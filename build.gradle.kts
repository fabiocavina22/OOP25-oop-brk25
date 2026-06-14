plugins {
    java
    application
    id("com.gradleup.shadow") version "8.3.6"
    id("org.danilopianini.gradle-java-qa") version "1.75.0"
}

group = "it.unibo.breakout"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

application {
    mainClass.set("it.unibo.breakout.App")
}

tasks.test {
    useJUnitPlatform()
}