plugins {
    java
    id("com.gradleup.shadow") version "9.6.1"
}

group = "mc.alk"
version = "3.9.17.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(8)
}

repositories {
    mavenCentral()
    maven("https://repo.hpfxd.com/releases/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/public/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    compileOnly("com.hpfxd.pandaspigot:pandaspigot-api:1.8.8-R0.1-SNAPSHOT")
}

tasks.shadowJar {
    archiveClassifier.set("")
}

