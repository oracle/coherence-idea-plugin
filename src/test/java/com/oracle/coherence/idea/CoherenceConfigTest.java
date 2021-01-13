/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 *
 */

package com.oracle.coherence.idea;

import org.jdom.Element;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
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
    public void shouldNotEnablePofGenerationByDefault() throws Exception
        {
        CoherenceConfig config = new CoherenceConfig();
        assertThat(config.isPofGeneratorEnabled(), is(false));
        }

    @Test
    public void shouldSetPofEnabled() throws Exception
        {
        CoherenceConfig config = new CoherenceConfig();
        config.setPofGeneratorEnabled(true);
        assertThat(config.isPofGeneratorEnabled(), is(true));
        }

    @Test
    public void shouldApplyChanges() throws Exception
        {
        CoherenceConfig changes = new CoherenceConfig();
        changes.setPofGeneratorEnabled(true);

        CoherenceConfig config = new CoherenceConfig();
        config.applyChanges(changes);

        assertThat(config.isPofGeneratorEnabled(), is(true));
        }

    @Test
    public void shouldCreateCopy() throws Exception
        {
        CoherenceConfig config = new CoherenceConfig();
        config.setPofGeneratorEnabled(true);

        CoherenceConfig copy = config.createCopy();
        assertThat(copy, is(not(sameInstance(config))));
        assertThat(config.isPofGeneratorEnabled(), is(true));
        }

    @Test
    public void shouldGetState() throws Exception
        {
        CoherenceConfig config = new CoherenceConfig();
        config.setPofGeneratorEnabled(true);

        Element state = config.getState();
        assertThat(state, is(notNullValue()));

        boolean pofEnabled = state.getAttribute(CoherenceConfig.ATTRIBUTE_POF_ENABLED).getBooleanValue();
        assertThat(pofEnabled, is(true));
        }

    @Test
    public void shouldCreateFromElement() throws Exception
        {
        Element element = new Element("coherence");
        element.setAttribute(CoherenceConfig.ATTRIBUTE_POF_ENABLED, "true");

        CoherenceConfig config = new CoherenceConfig(element);
        assertThat(config.isPofGeneratorEnabled(), is(true));
        }

    }
