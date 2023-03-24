[![Maven Central](https://img.shields.io/maven-central/v/com.telesign/telesign.svg)](https://mvnrepository.com/artifact/com.telesign/telesign) [![license](https://img.shields.io/github/license/TeleSign/java_telesign.svg)](https://github.com/TeleSign/java_telesign/blob/master/LICENSE.txt)

# Telesign Self-service Java SDK

[Telesign](https://telesign.com) connects, protects, and defends the customer experience with intelligence from billions of digital interactions and mobile signals. Through developer-friendly APIs that deliver user verification, digital identity, and omnichannel communications, we help the world's largest brands secure onboarding, maintain account integrity, prevent fraud, and streamline omnichannel engagement.

## Requirements

* **JDK 8+**
* ***_(Optional)_*** **Gradle** This package manager isn't required to use this SDK, but it is required to use the installation instructions below.
* ***_(Optional)_*** **Kotlin** This domain-specific langage (DSL) isn't required to use this SDK, but it is required to use the installation instructions below. These installation steps rely on using Kotlin as your DSL for the build script in the Gradle project. Some steps need to be adapted if you are using Groovy as your DSL.

> NOTE:
>
> These instructions are for MacOS. They will need to be adapted if you are installing on Windows.

## Installation

Follow these steps to add this SDK as a dependency to your project.

1. Create a directory where you want to create your Telesign integration. Skip this step if you already have created a project. If you plan to create multiple Java projects that use Telesign, we recommend that you group them within a `telesign_integrations` directory.

   ```
      cd ~/code/local
      mkdir telesign_integrations
      cd telesign_integrations
      mkdir {your project directory name}
      cd {your project directory name}
   ```

3. Create a new Gradle project.

   `gradle init`

4. Select the following options at each of the prompts that appear at the command line.

   * **Select type of project to generate:** application
   * **Select implementation language:** Java
   * **Split functionality across multiple subprojects?:** no
   * **Select build script DSL:** Kotlin
   * **Generate build using new APIs and behavior (some features may change in the next minor release)?:** yes
   * **Select test framework:** {your preferred test framework}
   * **Project name (default: {default project name}):** {your project name}
   * **Source package (default: {default source package name}):** {your source package name}

   You should then see a message that indicates the build was successful.

5. Add the following dependencies to the `dependencies` block in the file "app/build.gradle.kts" in your project:

   ```
   implementation("com.google.code.gson:gson:2.2.+")
   implementation ("com.squareup.okio:okio:2.9.0")
   implementation("com.telesign:telesign:2.3.0")
   ```
   
   That last dependency pulls in the Telesign Self-service Java SDK.

## Authentication

If you use a Telesign SDK to make your request, authentication is handled behind-the-scenes for you. All you need to provide is your Customer ID and API Key. The SDKs apply Digest authentication whenever they make a request to a Telesign service where it is supported. When Digest authentication is not supported, the SDKs apply Basic authentication.

## What's next

* Learn to send a request to Telesign with code with one of our [tutorials](https://developer.telesign.com/enterprise/docs/tutorials).
* Browse our [Developer Portal](https://developer.telesign.com) for tutorials, how-to guides, reference content, and more.
* Check out our [sample code](https://github.com/TeleSign/sample_code) on GitHub.
