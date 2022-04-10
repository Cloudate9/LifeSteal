import kr.entree.spigradle.kotlin.papermc

plugins {
    id("com.github.johnrengelman.shadow") version ("7.1.2")
    id("kr.entree.spigradle") version ("2.2.4")
    kotlin("jvm") version ("1.6.20")
}

group = "io.github.cloudate9.lifesteal"
version = "1.4.7"

repositories {
    maven("https://repo.mattstudios.me/artifactory/public/")
    mavenCentral()
    papermc()
}

dependencies {
    implementation("dev.triumphteam:triumph-gui:3.1.2")
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    compileJava {
        options.release.set(17)
    }

    prepareSpigotPlugins {
        setDependsOn(mutableListOf(shadowJar.get()))
    }

    runSpigot {
        jvmArgs = mutableListOf(
            "-Xmx2G",
            "-Xms2G",
            "-XX:+UseZGC",
            "-XX:+ZUncommit",
            "-XX:ZUncommitDelay=3600",
            "-XX:+ZProactive",
            "-XX:+AlwaysPreTouch",
            "-XX:+DisableExplicitGC",
        )
    }

    shadowJar {
        archiveFileName.set(rootProject.name + "-" + rootProject.version + ".jar")
        relocate("dev.triumphteam.gui", "$group.dependencies.gui")
        relocate("kotlin", "$group.dependencies.kotlin")
    }
}

spigot {
    authors = listOf("Cloudate9")
    apiVersion = "1.18"
    description = "A personal version of LifeSteal SMP"

    commands {
        create("hearts") {
            description = "Set, add, or remove hearts."
            usage = "/hearts add/remove/set <number of hearts>"
        }

        create("lootbox") {
            description = "Do the server lootbox thing"
            usage = "/lootbox new/status"
        }

        create("revive") {
            description = "Revive a player."
            usage = "/revive <player>"
        }

        create("withdraw") {
            description = "Withdraw hearts."
            usage = "/withdraw <number of hearts>"
        }
    }

    permissions {
        create("lifesteal.admin") {
            description = "Gives admin permission for lifesteal."
            defaults = "op"
        }

        create("lifesteal.lootbox.new") {
            description = "Gives permission to create a new loot box."
            defaults = "op"
        }

        create("lifesteal.lootbox.status") {
            description = "Gives permission to view the cords of a loot box."
            defaults = "true"
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
