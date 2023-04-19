plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.mongodb:mongo-java-driver:3.12.11")
    compile("com.example:library:1.0.0")
    api("com.example:library:1.0.0")
    runtimeOnly("com.example:library:1.0.0")

    compileOnly("com.example:dependency:1.0.0")
    provided("com.example:dependency:1.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testCompile("junit:junit:4.13.2")
    testApi("junit:junit:4.13.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    // Using Kotlin DSL with multiple dependencies and test dependencies:
    implementation(kotlin("stdlib")) // Kotlin standard library

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.30") // Full group and version
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.30") // Full group and version with underscore

    testImplementation("junit:junit:4.13.2") // JUnit 4 for testing
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.5.30") // Kotlin test library

    // Using full group, artifact, and version with commas for dependencies and test dependencies:
    implementation("org.jetbrains.kotlin", "kotlin-reflect", "1.5.30") // Full group, artifact, and version with commas
    implementation("org.jetbrains.kotlin", "kotlin-stdlib", "1.5.30") // Full group, artifact, and version with commas and underscore

    testImplementation("junit", "junit", "4.13.2") // JUnit 4 for testing with commas
    testImplementation("org.jetbrains.kotlin", "kotlin-test", "1.5.30") // Kotlin test library with commas

    // Using named parameters for dependencies and test dependencies:
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-reflect", version = "1.5.30") // Named parameters for group, artifact, and version
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib", version = "1.5.30") // Named parameters for group, artifact, and version with underscore

    testImplementation(group = "junit", name = "junit", version = "4.13.2") // JUnit 4 for testing with named parameters
    testImplementation(group = "org.jetbrains.kotlin", name = "kotlin-test", version = "1.5.30") // Kotlin test library with named parameters

    // implementation("com.example:library:1.0.0") {
    implementation("com.example:library:1.0.0") {
        exclude(group = "com.example", module = "excluded-dependency")
    }

    testImplementation("junit:junit:4.13.2") {
        exclude(module = "excluded-dependency")
    }

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}