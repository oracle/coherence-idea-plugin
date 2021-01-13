/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.idea.jps;

import com.intellij.compiler.instrumentation.InstrumentationClassFinder;

import com.oracle.coherence.common.schema.Schema;

import com.tangosol.io.pof.EvolvableObject;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.generator.PortableTypeGenerator;

import com.tangosol.io.pof.schema.annotation.internal.Instrumented;
import com.tangosol.net.CacheFactory;

import com.tangosol.util.Base;
import org.jetbrains.jps.incremental.BinaryContent;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.CompiledClass;

import org.jetbrains.org.objectweb.asm.ClassReader;
import org.jetbrains.org.objectweb.asm.ClassWriter;

import org.junit.Test;
import pof.Person;

import java.io.File;

import java.net.URISyntaxException;
import java.net.URL;

import java.net.URLClassLoader;
import java.nio.file.Files;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link PofGenerator}.
 *
 * @author Jonathan Knight  2020.07.02
 */
public class PofGeneratorTest
    {
    @Test
    public void shouldCreateSchema()
        {
        ClassLoader    loader      = getClass().getClassLoader();
        File           personClass = findPersonClass();
        Map<String, ?> env         = new HashMap<>();
        PofGenerator   generator   = new PofGenerator();
        Object         schema      = generator.ensureSchema(PortableTypeGenerator.class, personClass, loader, env);

        assertThat(schema, is(notNullValue()));
        assertThat(schema, is(instanceOf(Schema.class)));
        }

    @Test
    public void shouldNotInstrumentModuleInfo() throws Exception
        {
        PofGenerator  generator    = new PofGenerator();
        CompiledClass compileClass = mock(CompiledClass.class);

        when(compileClass.getClassName()).thenReturn("module-info");
        assertThat(generator.canInstrument(compileClass, 0), is(false));
        }

    @Test
    public void shouldNotInstrumentNonModuleInfo() throws Exception
        {
        PofGenerator  generator    = new PofGenerator();
        CompiledClass compileClass = mock(CompiledClass.class);

        when(compileClass.getClassName()).thenReturn("Foo");
        assertThat(generator.canInstrument(compileClass, 0), is(true));
        }

    @Test
    public void shouldInstrumentClass() throws Exception
        {
        String        className     = Person.class.getName();
        File          personClass  = findPersonClass();
        byte[]        bytes        = Files.readAllBytes(personClass.toPath());
        BinaryContent content      = new BinaryContent(bytes);
        File          dir          = Files.createTempDirectory("coherence-test").toFile();
        File          pkgDir       = new File(dir, "pof");
        File          outputFile   = new File(pkgDir, "Person.class");
        URL           urlCoherence = CacheFactory.class.getProtectionDomain().getCodeSource().getLocation();
        URL           urlPerson    = Person.class.getProtectionDomain().getCodeSource().getLocation();

        pkgDir.mkdirs();
        Files.copy(personClass.toPath(),outputFile.toPath());

        CompileContext             context = mock(CompileContext.class);
        CompiledClass              compiled = new CompiledClass(outputFile, personClass, Person.class.getName(), content);
        ClassReader                reader = new ClassReader(bytes);
        ClassWriter                writer = new ClassWriter(0);
        InstrumentationClassFinder finder = new InstrumentationClassFinder(new URL[]{urlPerson, urlCoherence});


        PofGenerator  generator    = new PofGenerator();
        BinaryContent instrumented = generator.instrument(context, compiled, reader, writer, finder);

        assertThat(instrumented, is(notNullValue()));

        byte[] instrumentedBytes = instrumented.toByteArray();
        Files.write(outputFile.toPath(), instrumentedBytes);

        ByteArrayClassLoader loader = new ByteArrayClassLoader(Collections.singletonMap(className, instrumentedBytes));
        Class<?> instrumentedClass = loader.findClass(className);

        assertThat(instrumentedClass.isAnnotationPresent(Instrumented.class), is(true));
        assertThat(PortableObject.class.isAssignableFrom(instrumentedClass), is(true));
        assertThat(EvolvableObject.class.isAssignableFrom(instrumentedClass), is(true));
        }

    private File findPersonClass()
        {
        try
            {
            URL  url  = Person.class.getProtectionDomain().getCodeSource().getLocation();
            File dir  = new File(url.toURI());
            return new File(dir, Person.class.getName().replaceAll("\\.", File.separator) + ".class");
            }
        catch (URISyntaxException e)
            {
            throw new RuntimeException(e);
            }
        }


    // ----- inner class: ByteArrayClassLoader ------------------------------

    public static class ByteArrayClassLoader
            extends URLClassLoader
        {
        public ByteArrayClassLoader(Map<String, byte[]> mapClasses)
            {
            super(new URL[0], Base.getContextClassLoader());
            m_mapClasses = mapClasses;
            }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException
            {
            byte[] classBytes = m_mapClasses.get(name);
            if (classBytes != null)
                {
                return defineClass(name, classBytes, 0, classBytes.length);
                }
            return super.findClass(name);
            }

        private final Map<String, byte[]> m_mapClasses;
        }
    }
