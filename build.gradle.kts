plugins {
    java
    application
}

group = "it.unibo.breakout"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // JUnit 5
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

// Fat-jar eseguibile (requisito P8)
tasks.jar {
    manifest {
        attributes["Main-Class"] = "it.unibo.breakout.App"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
