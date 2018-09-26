plugins {
    java
    id("com.github.johnrengelman.shadow") version "2.0.2"
}

tasks.withType<Jar> {
    manifest {
        attributes(mapOf(
                "Main-Class" to "webserver.Main"
        ))
    }
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compile(group = "org.apache.commons", name = "commons-lang3", version = "3.0")
    compile(group = "ch.qos.logback", name = "logback-classic", version = "1.2.3")
    compile(group = "commons-io", name = "commons-io", version = "2.6")
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_10
}