/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 *
 */

package com.oracle.coherence.idea;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for {@link CoherenceConfig}.
 *
 * @author Jonathan Knight  2020.07.03
 */
public class CoherenceConfigTest
    {
    @Test
    public void shouldEnablePofGenerationByDefault()
        {
        CoherenceConfig config = new CoherenceConfig();
        assertThat(config.isPofGeneratorEnabled(), is(true));
        }

    @Test
    public void shouldSetPofEnabled()
        {
        CoherenceConfig config = new CoherenceConfig();
        config.setPofGeneratorEnabled(true);
        assertThat(config.isPofGeneratorEnabled(), is(true));
        }

    @Test
    public void shouldApplyChanges()
        {
        CoherenceConfig changes = new CoherenceConfig();
        changes.setPofGeneratorEnabled(true);

        CoherenceConfig config = new CoherenceConfig();
        config.applyChanges(changes);

        assertThat(config.isPofGeneratorEnabled(), is(true));
        }

    @Test
    public void shouldCreateCopy()
        {
        CoherenceConfig config = new CoherenceConfig();
        config.setPofGeneratorEnabled(true);

        CoherenceConfig copy = config.createCopy();
        assertThat(copy, is(not(sameInstance(config))));
        assertThat(config.isPofGeneratorEnabled(), is(true));
        }
    }
