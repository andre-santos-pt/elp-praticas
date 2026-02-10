plugins {
    kotlin("jvm") version "2.1.10"
}

group = "pt.iscte"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    implementation("org.antlr:antlr4:4.13.2")
    implementation("com.github.javaparser:javaparser-symbol-solver-core:3.26.3")
    implementation("org.ow2.asm:asm:9.4")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}