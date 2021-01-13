/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 *
 */

package com.oracle.coherence.idea;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

import com.intellij.openapi.project.Project;

import com.intellij.serviceContainer.NonInjectable;

import org.jdom.DataConversionException;
import org.jdom.Element;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.jetbrains.jps.model.ex.JpsElementBase;

/**
 * The configuration for the Coherence plugin.
 *
 * @author Jonathan Knight  2020.07.02
 */
@State(
  name = CoherenceConfig.COHERENCE_SETTINGS,
  storages = {
    @Storage(CoherenceConfig.COHERENCE_CONFIG_FILE)
  }
)
public class CoherenceConfig
        extends JpsElementBase<CoherenceConfig>
        implements PersistentStateComponent<Element>
    {
    // ----- constructors ---------------------------------------------------

    public CoherenceConfig()
        {
        }

    @NonInjectable
    public CoherenceConfig(Element element)
        {
        if (element != null)
            {
            loadState(element);
            }
        }

    // ----- CoherenceConfig methods ----------------------------------------

    /**
     * Returns {@code true} if POF code generation is enabled, otherwise return {@code false}.
     *
     * @return  {@code true} if POF code generation is enabled, otherwise return {@code false}
     */
    public boolean isPofGeneratorEnabled()
        {
        return pofGeneratorEnabled;
        }

    /**
     * Enable or disable POF code generation.
     *
     * @param enabled  {@code true} to enable POF code generations, {@code false}
     *                 to disable POF code generation.
     */
    public void setPofGeneratorEnabled(boolean enabled)
        {
        pofGeneratorEnabled = enabled;
        }

    // ----- PersistentStateComponent methods -------------------------------

    @Nullable
    @Override
    public Element getState()
        {
        Element element = new Element(COHERENCE_SETTINGS);
        element.setAttribute(ATTRIBUTE_POF_ENABLED, String.valueOf(pofGeneratorEnabled));
        return element;
        }

    @Override
    public void loadState(@NotNull Element state)
        {
        try
            {
            pofGeneratorEnabled = state.getAttribute(ATTRIBUTE_POF_ENABLED).getBooleanValue();
            }
        catch (DataConversionException e)
            {
            throw new IllegalStateException(e);
            }
        }

    // ----- JpsElementBase methods -----------------------------------------

    @NotNull
    @Override
    public CoherenceConfig createCopy()
        {
        CoherenceConfig copy = new CoherenceConfig();
        copy.applyChanges(this);
        return copy;
        }

    @Override
    public void applyChanges(@NotNull CoherenceConfig modified)
        {
        this.pofGeneratorEnabled = modified.pofGeneratorEnabled;
        }

    // ----- helper methods -------------------------------------------------

    /**
     * Factory method to obtain the configuration for a project.
     *
     * @param project the IntelliJ project to load the configuration for
     *
     * @return and instance of {@link CoherenceConfig} for the project
     */
    @Nullable
    public static CoherenceConfig getInstance(Project project)
        {
        return ServiceManager.getService(project, CoherenceConfig.class);
        }

    // ----- constants ------------------------------------------------------

    /**
     * The name of the component that the configuration is for.
     */
    public static final String COHERENCE_SETTINGS = "Coherence";

    /**
     * The name of the configuration file.
     */
    public static final String COHERENCE_CONFIG_FILE = "Coherence.xml";

    /**
     * The attribute name in the configuration that holds the POF generator enabled flag.
     */
    public static final String ATTRIBUTE_POF_ENABLED = "pofGeneratorEnabled";

    // ----- data members ---------------------------------------------------

    /**
     * Flag to determine whether POF generation is enabled - disabled by default.
     */
    private boolean pofGeneratorEnabled = false;
    }
