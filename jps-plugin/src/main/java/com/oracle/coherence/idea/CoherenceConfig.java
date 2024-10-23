/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 *
 */

package com.oracle.coherence.idea;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElementChildRole;
import org.jetbrains.jps.model.JpsProject;
import org.jetbrains.jps.model.ex.JpsElementBase;
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase;

/**
 * The configuration for the Coherence plugin.
 *
 * @author Jonathan Knight  2020.07.02
 */
public class CoherenceConfig
        extends JpsElementBase<CoherenceConfig>
    {
    // ----- constructors ---------------------------------------------------

    public CoherenceConfig()
        {
        pofGeneratorEnabled = true;
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

    public void loadFrom(Element parent)
        {
        if (parent != null)
            {
            pofGeneratorEnabled = toBoolean(parent.getChild("pofGeneratorEnabled"));
            }
        else
            {
            pofGeneratorEnabled = false;
            }
        }

    public void saveTo(@NotNull Element parent)
        {
        Element pofEnabled = new Element("pofGeneratorEnabled");
        pofEnabled.setText(String.valueOf(pofGeneratorEnabled));
        parent.addContent(pofEnabled);
        }

    public static CoherenceConfig getSettings(JpsProject project) {
      CoherenceConfig config = project.getContainer().getChild(CoherenceConfig.ROLE);
      return config == null ? new CoherenceConfig() : config;
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
     * <p>Converts a String to a Boolean.</p>
     * <p/>
     * <p>{@code 'true'}, {@code 'on'} or {@code 'yes'}
     * (case insensitive) will return {@code true}.
     * {@code 'false'}, {@code 'off'} or {@code 'no'}
     * (case insensitive) will return {@code false}.
     * Otherwise, {@code null} is returned.</p>
     * <p/>
     * <p>NOTE: This returns null and will throw a NullPointerException if autoboxed to a boolean. </p>
     * <p/>
     * <pre>
     *   BooleanUtils.toBooleanObject(null)    = null
     *   BooleanUtils.toBooleanObject("true")  = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("false") = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("on")    = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("ON")    = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("off")   = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("oFf")   = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("blue")  = null
     * </pre>
     *
     * @param str the String to check
     * @return the Boolean value of the string, {@code null} if no match or {@code null} input
     */
    public static Boolean toBooleanObject(String str)
        {
        // Previously used equalsIgnoreCase, which was fast for interned 'true'.
        // Non interned 'true' matched 15 times slower.
        //
        // Optimisation provides same performance as before for interned 'true'.
        // Similar performance for null, 'false', and other strings not length 2/3/4.
        // 'true'/'TRUE' match 4 times slower, 'tRUE'/'True' 7 times slower.
        if (str.equals("true"))
            {
            return Boolean.TRUE;
            }
        if (str == null)
            {
            return null;
            }
        switch (str.length())
            {
            case 1:
            {
            char ch0 = str.charAt(0);
            if (ch0 == 'y' || ch0 == 'Y' ||
                    ch0 == 't' || ch0 == 'T')
                {
                return Boolean.TRUE;
                }
            if (ch0 == 'n' || ch0 == 'N' ||
                    ch0 == 'f' || ch0 == 'F')
                {
                return Boolean.FALSE;
                }
            break;
            }
            case 2:
            {
            char ch0 = str.charAt(0);
            char ch1 = str.charAt(1);
            if ((ch0 == 'o' || ch0 == 'O') &&
                    (ch1 == 'n' || ch1 == 'N'))
                {
                return Boolean.TRUE;
                }
            if ((ch0 == 'n' || ch0 == 'N') &&
                    (ch1 == 'o' || ch1 == 'O'))
                {
                return Boolean.FALSE;
                }
            break;
            }
            case 3:
            {
            char ch0 = str.charAt(0);
            char ch1 = str.charAt(1);
            char ch2 = str.charAt(2);
            if ((ch0 == 'y' || ch0 == 'Y') &&
                    (ch1 == 'e' || ch1 == 'E') &&
                    (ch2 == 's' || ch2 == 'S'))
                {
                return Boolean.TRUE;
                }
            if ((ch0 == 'o' || ch0 == 'O') &&
                    (ch1 == 'f' || ch1 == 'F') &&
                    (ch2 == 'f' || ch2 == 'F'))
                {
                return Boolean.FALSE;
                }
            break;
            }
            case 4:
            {
            char ch0 = str.charAt(0);
            char ch1 = str.charAt(1);
            char ch2 = str.charAt(2);
            char ch3 = str.charAt(3);
            if ((ch0 == 't' || ch0 == 'T') &&
                    (ch1 == 'r' || ch1 == 'R') &&
                    (ch2 == 'u' || ch2 == 'U') &&
                    (ch3 == 'e' || ch3 == 'E'))
                {
                return Boolean.TRUE;
                }
            break;
            }
            case 5:
            {
            char ch0 = str.charAt(0);
            char ch1 = str.charAt(1);
            char ch2 = str.charAt(2);
            char ch3 = str.charAt(3);
            char ch4 = str.charAt(4);
            if ((ch0 == 'f' || ch0 == 'F') &&
                    (ch1 == 'a' || ch1 == 'A') &&
                    (ch2 == 'l' || ch2 == 'L') &&
                    (ch3 == 's' || ch3 == 'S') &&
                    (ch4 == 'e' || ch4 == 'E'))
                {
                return Boolean.FALSE;
                }
            break;
            }
            }

        return null;
        }

    /**
     * <p>Converts a String to a boolean (optimised for performance).</p>
     * <p/>
     * <p>{@code 'true'}, {@code 'on'} or {@code 'yes'}
     * (case insensitive) will return {@code true}. Otherwise,
     * {@code false} is returned.</p>
     * <p/>
     * <p>This method performs 4 times faster (JDK1.4) than
     * {@code Boolean.valueOf(String)}. However, this method accepts
     * 'on' and 'yes' as true values.
     * <p/>
     * <pre>
     *   BooleanUtils.toBoolean(null)    = false
     *   BooleanUtils.toBoolean("true")  = true
     *   BooleanUtils.toBoolean("TRUE")  = true
     *   BooleanUtils.toBoolean("tRUe")  = true
     *   BooleanUtils.toBoolean("on")    = true
     *   BooleanUtils.toBoolean("yes")   = true
     *   BooleanUtils.toBoolean("false") = false
     *   BooleanUtils.toBoolean("x gti") = false
     * </pre>
     *
     * @param str the String to check
     * @return the boolean value of the string, {@code false} if no match or the String is null
     */
    public static boolean toBoolean(String str)
        {
        return toBooleanObject(str) == Boolean.TRUE;
        }

    public static boolean toBoolean(Element element)
        {
        return element != null && Boolean.TRUE.equals(toBooleanObject(element.getValue()));
        }

    // ----- constants ------------------------------------------------------

    public static final JpsElementChildRole<CoherenceConfig> ROLE = JpsElementChildRoleBase.create("OracleCoherence");

    // ----- data members ---------------------------------------------------

    /**
     * Flag to determine whether POF generation is enabled - disabled by default.
     */
    private boolean pofGeneratorEnabled = false;
    }
