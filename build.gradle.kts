plugins {
    java
    id("com.gradleup.shadow") version "9.6.1"
}

group = "mc.alk"
version = "4.0.0"

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
    maven("https://maven.elmakers.com/repository/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.extendedclip.com/releases/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://jitpack.io")
    mavenLocal()
}

dependencies {
    compileOnly("com.hpfxd.pandaspigot:pandaspigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly("net.milkbowl.vault:Vault:1.2.31") {
        isTransitive = false
    }
    compileOnly("me.clip:placeholderapi:2.12.3")
    compileOnly("mc.alk:BattleTracker:2.5.11.0")
    compileOnly("mc.alk:VirtualPlayers:1.10.0")
    compileOnly(files("libs/WorldEdit.jar"))
    compileOnly(files("libs/EssentialsX-2.20.1.jar"))

    implementation("mc.euro:Version:4.0.1")
    implementation("mc.euro:BukkitAdapter:1.2.8")
    implementation("mc.euro:BukkitInterface:4.0.1")
    implementation("mc.alk:BattleBukkitLib:4.5.2")
    implementation("mc.alk:BattlePluginUpdater:2.2.0")
    implementation("mc.alk:BattleScoreboardAPI:2.0")
    implementation("mc.alk:BattleWebAPI:2.0")
    implementation("org.battleplugins.arenaregenutil:ArenaRegenUtil:1.2.0")
    implementation("org.battleplugins.worldguardutil:WorldGuardUtil:1.1.2")
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand(
            mapOf(
                "project" to mapOf(
                    "name" to project.name,
                    "version" to project.version
                )
            )
        )
    }
}

// Equivalente a <maven.test.skip>true</maven.test.skip> en pom.xml:
// Las clases de prueba legacy requieren librerías mock internas no disponibles (mc.alk.tests.testbukkit).
tasks.compileTestJava {
    enabled = false
}

tasks.test {
    enabled = false
}

tasks.clean {
    delete(layout.projectDirectory.dir("target"))
}

tasks.shadowJar {
    destinationDirectory.set(layout.projectDirectory.dir("target"))
    archiveClassifier.set("")

    // Excluir clases de JUnit y artefactos que vienen dentro de BattleScoreboardAPI:2.0
    // (equivalente al filter del maven-shade-plugin en pom.xml)
    exclude("junit/**")
    exclude("stylesheet.css")
    exclude("META-INF/maven/**")

    relocate("mc.euro.version", "mc.alk.arena.alib.version")
    relocate("mc.euro.bukkitadapter", "mc.alk.arena.alib.bukkitadapter")
    relocate("mc.euro.bukkitinterface", "mc.alk.arena.alib.bukkitinterface")
    relocate("mc.alk.battlebukkitlib", "mc.alk.arena.alib.battlebukkitlib")
    relocate("mc.alk.battlepluginupdater", "mc.alk.arena.alib.battlepluginupdater")
    relocate("mc.alk.battlescoreboardapi", "mc.alk.arena.alib.battlescoreboardapi")
    relocate("mc.alk.battlewebapi", "mc.alk.arena.alib.battlewebsapi")
    relocate("org.battleplugins.arenaregenutil", "mc.alk.arena.alib.arenaregenutil")
    relocate("org.battleplugins.worldguardutil", "mc.alk.arena.alib.worldguardutil")
}
