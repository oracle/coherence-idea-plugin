/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 *
 */

package com.oracle.coherence.idea.jps;

import org.jetbrains.annotations.NotNull;

import org.jetbrains.jps.model.serialization.JpsModelSerializerExtension;
import org.jetbrains.jps.model.serialization.JpsProjectExtensionSerializer;

import java.util.Collections;
import java.util.List;

/**
 * A {@link JpsModelSerializerExtension} to load the Coherence
 * build configuration serializer.
 *
 * @author Jonathan Knight  2020.07.02
 */
public class CoherenceJpsModelSerializerExtension
        extends JpsModelSerializerExtension
    {
    @NotNull
    @Override
    public List<? extends JpsProjectExtensionSerializer> getProjectExtensionSerializers()
        {
        return Collections.singletonList(new CoherenceJpsProjectSerializer());
        }

    }
