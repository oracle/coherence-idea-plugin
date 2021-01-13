/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.idea.jps;

import com.oracle.coherence.idea.CoherenceConfig;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElementChildRole;
import org.jetbrains.jps.model.JpsProject;
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase;

import org.jetbrains.jps.model.serialization.JpsProjectExtensionSerializer;

/**
 * A {@link JpsProjectExtensionSerializer} to load the Coherence configuration
 * for a project at build time.
 *
 * @author Jonathan Knight 2020.07.02
 */
public class CoherenceJpsProjectSerializer
        extends JpsProjectExtensionSerializer
    {
    // ----- constructors ---------------------------------------------------

    /**
     * Create a {@link CoherenceJpsProjectSerializer}.
     */
    public CoherenceJpsProjectSerializer()
        {
        super(CoherenceConfig.COHERENCE_CONFIG_FILE, CoherenceConfig.COHERENCE_SETTINGS);
        }

    // ----- JpsGlobalExtensionSerializer methods ---------------------------

    @Override
    public void loadExtension(@NotNull JpsProject project, @NotNull Element element)
        {
        project.getContainer().setChild(CONFIG, new CoherenceConfig(element));
        }

    @Override
    public void saveExtension(@NotNull JpsProject project, @NotNull Element element)
        {
        }

    // ----- data members ---------------------------------------------------

    /**
     * The key to the {@link CoherenceConfig} instance for a project.
     */
    public static final JpsElementChildRole<CoherenceConfig> CONFIG
            = JpsElementChildRoleBase.create(CoherenceConfig.COHERENCE_SETTINGS);
    }
