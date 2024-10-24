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
import org.jetbrains.jps.model.JpsProject;
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
    /**
     * Create a {@link CoherenceJpsProjectSerializer}.
     */
    public CoherenceJpsProjectSerializer()
        {
        super(CONFIG_FILE_NAME, NAME);
        }

    @Override
    public void loadExtension(@NotNull JpsProject project, @NotNull Element element)
        {
        CoherenceConfig config  = new CoherenceConfig();
        config.loadFrom(element);
        project.getContainer().setChild(CoherenceConfig.ROLE, config);
        }

    // ----- data members ---------------------------------------------------

    public static final String NAME = "OracleCoherence";

    public static final String CONFIG_FILE_NAME = "oracleCoherence.xml";
    }
