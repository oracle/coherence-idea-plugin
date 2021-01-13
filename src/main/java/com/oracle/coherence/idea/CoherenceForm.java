/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 *
 */

package com.oracle.coherence.idea;

import com.intellij.openapi.options.ConfigurationException;

import javax.swing.*;

/**
 * The Coherence config UI.
 *
 * @author Jonathan Knight  2020.07.02
 */
public class CoherenceForm
    {
    // ----- CoherenceForm methods ------------------------------------------

    public JPanel getPanel()
        {
        return panel;
        }

    /**
     * Determine whether any settings have been modified on the form when
     * compared to the specified configuration.
     *
     * @param config  the configuration to compare to
     *
     * @return  {@code true} if any settings have been modified, otherwse {@code false}
     */
    public boolean isModified(CoherenceConfig config)
        {
        if (config.isPofGeneratorEnabled() != m_enablePOFGenerationCheckBox.isSelected())
            {
            return true;
            }
        return false;
        }

    public void applyEditorTo(CoherenceConfig config) throws ConfigurationException
        {
        config.setPofGeneratorEnabled(m_enablePOFGenerationCheckBox.isSelected());
        }

    public void resetEditorFrom(CoherenceConfig config)
        {
        m_enablePOFGenerationCheckBox.setSelected(config.isPofGeneratorEnabled());
        }

    public static void main(String[] args)
        {
        JFrame frame = new JFrame("CoherenceForm");
        frame.setContentPane(new CoherenceForm().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        }

    // ----- data members ---------------------------------------------------

    private JPanel panel;
    private JCheckBox m_enablePOFGenerationCheckBox;
    }
