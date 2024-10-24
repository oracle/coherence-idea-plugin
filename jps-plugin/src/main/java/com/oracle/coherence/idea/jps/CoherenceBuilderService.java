/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 *
 */

package com.oracle.coherence.idea.jps;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildTargetType;
import org.jetbrains.jps.builders.java.JavaModuleBuildTargetType;
import org.jetbrains.jps.incremental.BuilderService;
import org.jetbrains.jps.incremental.ModuleLevelBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@link BuilderService} implementation that provides
 * Coherence specific builders to the IntelliJ build process.
 *
 * @author Jonathan Knight  2020.07.02
 */
public class CoherenceBuilderService
        extends BuilderService
    {
    @Override
    public @NotNull List<? extends ModuleLevelBuilder> createModuleLevelBuilders()
        {
        return Collections.singletonList(new PofGenerator());
        }

    @Override
    public @NotNull List<? extends BuildTargetType<?>> getTargetTypes()
        {
        return new ArrayList<>(JavaModuleBuildTargetType.ALL_TYPES);
        }
    }
