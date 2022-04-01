import kr.entree.spigradle.kotlin.papermc

plugins {
    id("com.github.johnrengelman.shadow") version ("7.1.2")
    id("kr.entree.spigradle") version ("2.2.4")
    kotlin("jvm") version ("1.6.10")
}

group = "io.github.cloudate9.lifesteal"
version = "1.3.1"

repositories {
    maven("https://nexus.sirblobman.xyz/repository/public/")
    mavenCentral()
    papermc()
}

dependencies {
    compileOnly("com.github.sirblobman.combatlogx:api:11.0.0.0-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    compileJava {
        options.release.set(17)
    }

    shadowJar {
        archiveFileName.set(rootProject.name + "-" + rootProject.version + ".jar")
        relocate("kotlin", "io.github.cloudate9.lifesteal.dependencies")
    }
}

spigot {
    authors = listOf("Cloudate9")
    apiVersion = "1.18"
    description = "A personal version of LifeSteal SMP"
    depends("CombatLogX")

    commands {
        create("withdraw") {
            description = "Withdraw hearts."
            usage = "/withdraw <number of hearts>"
        }

        create("revive") {
            description = "Revive a player."
            usage = "/revive <player>"
        }

        create("restore") {
            description = "Restore back to 10 hearts"
            usage = "/restore <name of player>"
        }
    }

    permissions {
        create("lifesteal.admin") {
            description = "Gives admin permission for lifesteal."
            defaults = "op"
        }

        create("lifesteal.revive") {
            description = "Gives permission to revive a player."
            defaults = "true"
        }

        create("lifesteal.withdraw") {
            description = "Gives permission to withdraw hearts."
            defaults = "true"
        }
    }
}
