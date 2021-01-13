/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 *
 */

package com.oracle.coherence.idea.jps;

import com.intellij.compiler.instrumentation.InstrumentationClassFinder;
import com.intellij.openapi.diagnostic.Logger;
import com.oracle.coherence.idea.CoherenceConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.ModuleChunk;
import org.jetbrains.jps.incremental.BinaryContent;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.CompiledClass;
import org.jetbrains.jps.incremental.instrumentation.BaseInstrumentingBuilder;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CompilerMessage;
import org.jetbrains.jps.model.JpsProject;
import org.jetbrains.org.objectweb.asm.ClassReader;
import org.jetbrains.org.objectweb.asm.ClassWriter;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * A {@link BaseInstrumentingBuilder} that uses the Coherence PortableTypeGenerator
 * class to instrument a class byte-code with POF methods.
 * <p>
 * The instance of the PortableTypeGenerator used will be obtained from the Coherence
 * libraries present in the project dependencies. This is to ensure that the code
 * generated corresponds to the version of Coherence being used by the project and
 * is not dictated by the version of the plugin.
 *
 * @author Jonathan Knight 2020.07.02
 */
public class PofGenerator
        extends BaseInstrumentingBuilder
    {
    @Override
    protected boolean canInstrument(CompiledClass compiledClass, int classFileVersion)
        {
        // don't try to instrument a module-info class
        return !"module-info".equals(compiledClass.getClassName());
        }

    @Override
    protected @Nullable BinaryContent instrument(CompileContext             context,
                                                 CompiledClass              compiled,
                                                 ClassReader                reader,
                                                 ClassWriter                writer,
                                                 InstrumentationClassFinder finder)
        {
        BinaryContent  instrumented = null;
        ClassLoader    loader       = finder.getLoader();
        Thread         thread       = Thread.currentThread();
        ClassLoader    loaderThread = thread.getContextClassLoader();
        Properties     properties   = new Properties();
        URLClassLoader loaderParent = (URLClassLoader) loader.getParent();
        List<File>     libs         = Arrays.stream(loaderParent.getURLs())
                                            .map(this::toFile)
                                            .filter(Objects::nonNull)
                                            .collect(Collectors.toList());

        try
            {
            thread.setContextClassLoader(loader);

            Class<?> clsPofGenerator = findGeneratorClass(loader);

            if (clsPofGenerator != null)
                {
                File                fileClass = compiled.getOutputFile();
                BinaryContent       content   = compiled.getContent();
                byte[]              abBytes   = content.getBuffer();
                int                 nOffset   = content.getOffset();
                int                 nLen      = content.getLength();
                byte[]              abInst    = null;
                Method              method    = getInstrumentMethod(clsPofGenerator);
                Map<String, Object> env       = new HashMap<>();

                env.put("libs", libs);
                env.put("schema", ensureSchema(clsPofGenerator, fileClass, loader, env));

                if (method != null)
                    {
                    abInst = (byte[]) method.invoke(null, fileClass, abBytes, nOffset, nLen, properties, env);
                    schema = env.get("schema");
                    }

                if (abInst != null)
                    {
                    String msg = "Instrumented " + compiled.getClassName();
                    String path = compiled.getOutputFile().getCanonicalPath();
                    CompilerMessage compileMsg = new CompilerMessage("pof", BuildMessage.Kind.INFO, msg, path);

                    context.processMessage(compileMsg);
                    instrumented = new BinaryContent(abInst);
                    }
                }
            else
                {
                // no PortableTypeGenerator class found - Coherence might not be on this project/module's classpath
                LOGGER.info("Skipped POF generation - could not find the PortableTypeGenerator on the module's classpath");
                }
            }
        catch (Exception e)
            {
            LOGGER.error(e);

            Throwable cause = e.getCause();
            if (cause != null)
                {
                LOGGER.error(cause);
                }

            throw new RuntimeException("POF generation internal error - " + e.getMessage(), e);
            }
        finally
            {
            thread.setContextClassLoader(loaderThread);
            }

        return instrumented;
        }

    @Override
    protected boolean isEnabled(CompileContext context, ModuleChunk chunk)
        {
        JpsProject      project = context.getProjectDescriptor().getProject();
        CoherenceConfig config  = project.getContainer().getChild(CoherenceJpsProjectSerializer.CONFIG);
        return config != null && config.isPofGeneratorEnabled();
        }

    @Override
    protected String getProgressMessage()
        {
        return "Generating Coherence POF byte-code...";
        }

    @Override
    public @NotNull String getPresentableName()
        {
        return "Coherence POF";
        }

    // ----- helper methods -------------------------------------------------

    /**
     * Find the POF generator class to use.
     *
     * @param loader  the {@link ClassLoader} to use to find the POF Generator
     *
     * @return  the POF Generator class or {@code null} if no
     *          POF Generator is on the class path
     */
    private Class<?> findGeneratorClass(ClassLoader loader)
        {
        for (String sCls : CLASS_NAMES)
            {
            try
                {
                return loader.loadClass(sCls);
                }
            catch (ClassNotFoundException e)
                {
                // ignored - class is not on class path
                }
            }

        return null;
        }

    Object ensureSchema(Class<?> clsPofGenerator, File fileClass, ClassLoader loader, Map<String, ?> mapEnv)
        {
        if (previousClassLoader == null || !previousClassLoader.equals(loader))
            {
            schema = null;
            }

        previousClassLoader = loader;

        if (schema == null)
            {
            try
                {
                Method createSchema = clsPofGenerator.getMethod("createSchema", File.class, Map.class);

                schema = createSchema.invoke(null, fileClass, mapEnv);
                }
            catch (Exception e)
                {
                // ignored
                }
            }

        return schema;
        }

    /**
     * Find the instrumentation method.
     *
     * @param cls  the {@link Class} to find the method on
     *
     * @return  the instrumentation method reference
     */
    private Method getInstrumentMethod(Class<?> cls)
        {
        try
            {
            return cls.getMethod("instrumentClass", File.class, byte[].class, int.class,
                                 int.class, Properties.class, Map.class);
            }
        catch (Exception e)
            {
            // ignore exception, method does not exist
            return null;
            }
        }

    /**
     * Convert the specified {@link URL} to a {@link File}.
     *
     * @param url the {@link URL}
     * @return a {@link File} from the {@link URL}
     */
    private File toFile(URL url)
        {
        try
            {
            URI uri = url.toURI();
            String sName = uri.getSchemeSpecificPart();
            File file = new File(sName);

            return file.exists() ? file : null;
            }
        catch (URISyntaxException e)
            {
            LOGGER.error(e);
            return null;
            }
        }

    // ----- constants ------------------------------------------------------

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = Logger.getInstance(PofGenerator.class);

    /**
     * The array of possible POF generator class names.
     */
    private static final String[] CLASS_NAMES =
            {
            "com.tangosol.io.pof.generator.PortableTypeGenerator"
            };

    // ----- data members ---------------------------------------------------

    /**
     * The previous {@link ClassLoader}.
     */
    private ClassLoader previousClassLoader = null;

    /**
     * The POF Schema.
     */
    private Object schema = null;
    }
