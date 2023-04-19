# Regular Expression Dependencies
## What is this project about?

Get Maven / Gradle (Groovy or Kotlin) dependencies from build files.

## Gradle
### Groovy and Kotlin
Support only:
- get dependencies from build.gradle

Not support:
- if dependencies stored in another gradle file
- if dependencies stored in properties file
- if dependencies versions stored in another block instead of dependencies block

## Maven
Support only:
- get dependencies from pom.xml
- support getting versions from properties tag

