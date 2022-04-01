package io.github.cloudate9.lifesteal.commands

import io.github.cloudate9.lifesteal.LifeSteal
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Revive(private val lifeSteal: LifeSteal) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {

            sender.sendMessage(
                Component.text("Only players can use /withdraw")
                    .color(TextColor.color(Integer.parseInt("FF5555", 16)))
            )

            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage(
                Component.text("Incorrect usage: command is /restore (player name).")
                    .color(TextColor.color(Integer.parseInt("FF5555", 16)))
            )
            return true
        }

        if (sender.hasPermission("lifesteal.revive")) {

            if (!sender.inventory.containsAtLeast(LifeSteal.reviveItemModel, 1)) {
                sender.sendMessage(
                    Component.text("You need a revive item to revive a player!")
                        .color(TextColor.color(Integer.parseInt("FF5555", 16)))
                )
                return true
            }

            val playerToRevive = Bukkit.getOfflinePlayerIfCached(args[0])
            if (playerToRevive == null) {
                sender.sendMessage(
                    Component.text("${args[0]} not found!")
                        .color(TextColor.color(Integer.parseInt("FF5555", 16)))
                )
                return true
            }

            val bannedList = lifeSteal.config.getList("banned")!!

            if (!bannedList.contains(playerToRevive.uniqueId.toString())) {
                sender.sendMessage(
                    Component.text("${args[0]} is not banned!")
                        .color(TextColor.color(Integer.parseInt("FF5555", 16)))
                )
                return true
            }

            bannedList.remove(playerToRevive.uniqueId.toString())
            lifeSteal.config.set("banned", bannedList)
            lifeSteal.saveConfig()

            sender.sendMessage(
                Component.text("Revived ${args[0]} to 1 heart.")
                    .color(TextColor.color(Integer.parseInt("55FF55", 16)))
            )

            sender.inventory.remove(LifeSteal.reviveItemModel)


        }
        return true
    }
}