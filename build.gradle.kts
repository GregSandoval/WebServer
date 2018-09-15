plugins {
    java
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile(group = "org.apache.commons", name = "commons-lang3", version = "3.0")
    compile(group = "ch.qos.logback", name = "logback-classic", version = "1.2.3")
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_10
}