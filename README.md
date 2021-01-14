<!--

  Copyright (c) 2020 Oracle and/or its affiliates.

  Licensed under the Universal Permissive License v 1.0 as shown at
  https://oss.oracle.com/licenses/upl.

-->

-----
<img src=https://oracle.github.io/coherence/assets/images/logo-red.png><img>

# Coherence IntelliJ Plugin

![Build](https://github.com/oracle/coherence-idea-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/com.oracle.coherence.coherence-idea.svg)](https://plugins.jetbrains.com/plugin/com.oracle.coherence.coherence-idea)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/com.oracle.coherence.coherence-idea.svg)](https://plugins.jetbrains.com/plugin/com.oracle.coherence.coherence-idea)
[![License](http://img.shields.io/badge/license-UPL%201.0-blue.svg)](https://oss.oracle.com/licenses/upl/)

<!-- Plugin description -->
This plugin adds features to IntelliJ to assist in development of applications using Oracle Coherence.

Features:

* POF code generation for suitably annotated classes.

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "coherence-idea"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/oracle/coherence-idea-plugin/releases/latest) and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Usage

The POF code generator plugin uses the Coherence POF code generator to instrument classes tha have been annotated with
the corresponding POF annotations. The plugin is a build service plugin that will instrument classes as part of the
IntelliJ build process.

The plugin itself does not contain a `coherence.jar` or the POF code generator, it 
will use the code generator built into the version of Coherence that the current project depends on. 
This means that there is no need to upgrade the IntelliJ plugin for different versions of Coherence. 

For full documentation on the Coherence POF code generator see **ToDo: Add Link Here**

The POF plugin is disabled by default and must be enabled for each IntelliJ project.

* Open the IntelliJ preference pane and go to the Oracle Coherence panel under Languages & Frameworks,
  
  <kbd>Preferences</kbd> > <kbd>Languages & Frameworks</kbd> > <kbd>Oracle Coherence</kbd>

* Check the `Enable POF code generation` check box.  

![preferences](images/idea-preferences.png)


---
This plugin has been created using the 
[IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
