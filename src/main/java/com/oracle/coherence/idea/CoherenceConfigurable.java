/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 *
 */

package com.oracle.coherence.idea;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;

import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * The Coherence configurable plugin component.
 * <p>
 * This class configures the Coherence plugin settings for a
 * single {@link Project}.
 *
 * @author Jonathan Knight  2020.07.02
 */
public class CoherenceConfigurable
        implements SearchableConfigurable
    {
    // ----- constructors ---------------------------------------------------

    /**
     * Create a {@link CoherenceConfigurable} for a project.
     *
     * @param project  the {@link Project} that this configuration is for
     */
    public CoherenceConfigurable(@NotNull Project project)
        {
        this.project = project;
        }

    // ----- SearchableConfigurable methods ---------------------------------

    @Override
    public @NotNull String getId()
        {
        return "coherence.settings";
        }

    @Override
    public String getDisplayName()
        {
        return "Oracle Coherence";
        }

    @Override
    public @Nullable JComponent createComponent()
        {
        if (form == null)
            {
            form = new CoherenceForm();
            }
        reset();
        return form.getPanel();
        }

    @Override
    public boolean isModified()
        {
        return form != null && form.isModified(getConfig());
        }

    @Override
    public void apply() throws ConfigurationException
        {
        if (form != null)
            {
            form.applyEditorTo(getConfig());
            }
        }

    @Override
    public void reset()
        {
        if (form != null)
            {
            form.resetEditorFrom(getConfig());
            }
        }

    @Override
    public void disposeUIResources()
        {
        form = null;
        }

    // ----- helper methods -------------------------------------------------

    private CoherenceConfig getConfig()
        {
        return CoherenceConfig.getInstance(project);
        }

    // ----- data members ---------------------------------------------------

    /**
     * The {@link Project} that the Coherence plugin settings are for.
     */
    private final Project project;

    /**
     * The UI form that will be displayed to allow settings to be changed.
     */
    private CoherenceForm form;
    }
