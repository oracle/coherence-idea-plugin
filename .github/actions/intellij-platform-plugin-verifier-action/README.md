# IntelliJ Platform Plugin Verifier Action
A [GitHub Action](https://help.github.com/en/actions) for executing the [JetBrains intellij-plugin-verifier](https://github.com/JetBrains/intellij-plugin-verifier).

[![GitHub Marketplace](https://img.shields.io/badge/Marketplace-v1.0.3-blue.svg?logo=github&logoColor=white&style=flat)](https://github.com/marketplace/actions/intellij-platform-plugin-verifier)

# Usage
Add the action to your [GitHub Action Workflow file](https://help.github.com/en/actions/configuring-and-managing-workflows/configuring-a-workflow#creating-a-workflow-file) - the only thing you _need_ to specify are the JetBrains products & versions you wish to run against.

A minimal example of a workflow step is below:
```yaml
  - name: Verify Plugin on IntelliJ Platforms
    uses: ChrisCarini/intellij-platform-plugin-verifier-action@v1.0.3
    with:
      ide-versions: |
        ideaIC:2019.3
```

## Installation

1) Create a `.yml` (or `.yaml`) file in your GitHub repository's `.github/workflows` folder. We will call this file `compatibility.yml` below.
1) Copy the below contents into `compatibility.yml` 
    ```yaml
    name: IntelliJ Platform Plugin Compatibility
    
    on:
      push:
    
    jobs:
      compatibility:
        name: Ensure plugin compatibility against 2019.3 for IDEA Community, IDEA Ultimate, PyCharm Community, GoLand, CLion, and the latest EAP snapshot of IDEA Community.
        runs-on: ubuntu-latest
        steps:
          - name: Check out repository
            uses: actions/checkout@v1
    
          - name: Setup Java 1.8
            uses: actions/setup-java@v1
            with:
              java-version: 1.8
    
          - name: Build the plugin using Gradle
            run: ./gradlew buildPlugin
    
          - name: Verify Plugin on IntelliJ Platforms
            id: verify
            uses: ChrisCarini/intellij-platform-plugin-verifier-action@v1.0.3
            with:
              ide-versions: |
                ideaIC:2019.3
                ideaIU:2019.3
                pycharmPC:2019.3
                goland:2019.3
                clion:2019.3
                ideaIC:LATEST-EAP-SNAPSHOT
    
          - name: Get log file path and print contents
            run: |
              echo "The verifier log file [${{steps.verify.outputs.verification-output-log-filename}}] contents : " ;
              cat ${{steps.verify.outputs.verification-output-log-filename}}
    ```

## Options

This GitHub Action exposes 3 input options, only one of which is required.

| Input | Description | Usage | Default |
| :---: |  :--------- | :---: | :-----: |
| `verifier-version`  | The version of the [JetBrains intellij-plugin-verifier](https://github.com/JetBrains/intellij-plugin-verifier). The default of `LATEST` will automatically pull the most recently released version from GitHub - a specific version of the `intellij-plugin-verifier` can be optionally be pinned if desired. | *Optional* | `LATEST` |
| `plugin-location`  | The path to the `zip`-distribution of the plugin(s), generated by executing `./gradlew buildPlugin` | *Optional* | `build/distributions/*.zip` |
| `ide-versions`  | Releases of IntelliJ Platform IDEs and versions that should be used to validate against, formatted as a multi-line string as shown in the examples. Formatted as `<ide>:<version>` - see below for details. If you would prefer to have the list of IDE and versions stored in a file, see the **Configuration file for `<ide>:<version>`** section below for details. | *Required* | |

An example using all the available options is below:
```yaml
  - name: Verify Plugin on IntelliJ Platforms
    id: verify
    uses: ChrisCarini/intellij-platform-plugin-verifier-action@v1.0.3
    with:
      verifier-version: '1.230'
      plugin-location: 'build/distributions/sample-intellij-plugin-*.zip'
      ide-versions: |
        ideaIC:LATEST-EAP-SNAPSHOT
``` 

### `verifier-version`

This optional input allows users to pin a specific version of `intellij-plugin-verifier` to be used during validation.

### `plugin-location`

This optional input allows users to specify a different location for the plugin(s) `.zip` file. The default 
assumes that [gradle-intellij-plugin](https://github.com/JetBrains/gradle-intellij-plugin/) is being used to build
 the plugin(s).

### `ide-versions`

This required input sets which IDEs and versions the plugins will be validated against.

You can identify the value for `<ide>` and `<version>` as follows.

1) Navigate to the [IntelliJ Releases Repository](https://www.jetbrains.com/intellij-repository/releases/)
1) Find the IDE and version you wish to use.
1) Copy the URL for the `.zip`.
1) Take **only** the `.zip` filename from the URL; example below:
    ```
    https://www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/pycharm/pycharmPY/2019.3/pycharmPY-2019.3.zip
   ```
    becomes
    ```
    pycharmPY-2019.3.zip
    ```
1) Replace the `-` with a `:`, and remove the `.zip` from the end; example below:
    ```
    pycharmPY-2019.3.zip
   ```
    becomes
    ```
    pycharmPY:2019.3
    ```
1) This is the value you will use in `ide-versions`.

#### Some `<ide>` options
* [CLion](https://www.jetbrains.com/clion/) = `clion`
* [GoLand](https://www.jetbrains.com/go/) = `goland`
* [IntelliJ IDEA](https://www.jetbrains.com/idea/)
    * IntelliJ IDEA Community = `ideaIC`
    * IntelliJ IDEA Ultimate = `ideaIU`
* [PyCharm](https://www.jetbrains.com/pycharm/)
    * PyCharm Community = `pycharmPC`
    * PyCharm Professional = `pycharmPY`
* [Rider](https://www.jetbrains.com/rider/) = `riderRD`

#### Some `<version>` options
* Major versions (ie, `2019.3`)
* Minor versions (ie, `2019.3.4`)
* Specific build versions (ie, `193.6911.18`)
* `SNAPSHOT` versions
    * versions ending in `*-SNAPSHOT`
    * versions ending in `*-EAP-SNAPSHOT`
    * versions ending in `*-EAP-CANDIDATE-SNAPSHOT`
    * versions ending in `*-CUSTOM-SNAPSHOT`
* Latest EAP version (ie, `LATEST-EAP-SNAPSHOT`)

#### Configuration file for `<ide>:<version>`
If you would like to keep your GitHub Action workflow file tidy and free from constant changes, you can pass a relative 
file path to a file containing the IDE and versions. Below are the respective excerpts to use this feature.

**Workflow File:**
```yaml
- name: Verify plugin on IntelliJ Platforms
  id: verify
  uses: ChrisCarini/intellij-platform-plugin-verifier-action@latest
  with:
    ide-versions: .github/workflows/ide_versions_file.txt
```
**`.github/workflows/ide_versions_file.txt`**
```
ideaIC:2019.3
ideaIU:2019.3
pycharmPC:2019.3
goland:2019.3
clion:2019.3
ideaIC:LATEST-EAP-SNAPSHOT
```

(**Note:** The sample above will yield an execution identical to the one provided in the `Installation` section above.)

## Results
The results of the execution are captured in a file for use in subsequent steps if you so choose.

You will need to give the `intellij-platform-plugin-verifier-action` step an `id`.

You can then access the verifier output file by using `${{steps.<id>.outputs.verification-output-log-filename}}`.

In the below example, we use set the `id` to `verify` - this example will print the filename as well as the contents of the file as a subsequent step to the validation:

```yaml
      - name: Verify Plugin on IntelliJ Platforms
        id: verify
        uses: ChrisCarini/intellij-platform-plugin-verifier-action@v1.0.3
        with:
          ide-versions: |
            ideaIC:2019.3

      - name: Get log file path and print contents
        run: |
          echo "The verifier log file [${{steps.verify.outputs.verification-output-log-filename}}] contents : " ;
          cat ${{steps.verify.outputs.verification-output-log-filename}}
```

(**Note:** The file contents will include both `stdout` and `stderr` output from the plugin verification CLI.)


# Examples

As examples of using this plugin you can check out following projects:

- [Automatic Power Saver](https://plugins.jetbrains.com/plugin/11941-automatic-power-saver) - Automatically enable / disable power save mode on window focus changes.
- [Environment Variable Settings Summary](https://plugins.jetbrains.com/plugin/10998-environment-variable-settings-summary) - Provides all system environment variables for troubleshooting.
- [Logshipper](https://plugins.jetbrains.com/plugin/11195-logshipper) - Ship your IDE logs to a logstash service.
- [intellij-sample-notification](https://plugins.jetbrains.com/plugin/10924-intellij-sample-notification) - Displays a simple notification upon Project Open.

# Contributing

Contributions welcomed! Feel free to open a PR, or issue.

## Debugging
This action has [GitHub Actions Debug Logging](https://help.github.com/en/actions/configuring-and-managing-workflows/managing-a-workflow-run#enabling-step-debug-logging).

To enable, set the following secret in the repository that contains the workflow using this action to `true`.
- `ACTIONS_STEP_DEBUG`

You can find this under the repositories `Settings -> Secrets` menu.
