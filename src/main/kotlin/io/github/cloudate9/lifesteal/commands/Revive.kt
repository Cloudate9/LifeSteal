package io.github.cloudate9.lifesteal.commands

import io.github.cloudate9.lifesteal.LifeSteal
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

class Revive(private val lifeSteal: LifeSteal, private val miniMessage: MiniMessage) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {

            sender.sendMessage(
                miniMessage.deserialize("<red>Only players can use /withdraw</red>")
            )

            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage(
                Component.text("Incorrect usage: command is /restore <player name>.")
            )
            return true
        }

        if (sender.hasPermission("lifesteal.revive")) {

            if (!sender.inventory.containsAtLeast(lifeSteal.reviveItemModel, 1)) {
                sender.sendMessage(
                    miniMessage.deserialize("<red>You need a revive item to revive a player!</red>")
                )
                return true
            }

            val playerToRevive = Bukkit.getOfflinePlayerIfCached(args[0])
            if (playerToRevive == null) {
                sender.sendMessage(
                    miniMessage.deserialize("<red>${args[0]} not found!</red>")
                )
                return true
            }

            val bannedList = lifeSteal.config.getList("banned")!!

            if (!bannedList.contains(playerToRevive.uniqueId.toString())) {
                sender.sendMessage(
                    miniMessage.deserialize("<red>${args[0]} is not banned!</red>")
                )
                return true
            }

            bannedList.remove(playerToRevive.uniqueId.toString())
            lifeSteal.config.set("banned", bannedList)
            lifeSteal.saveConfig()

            sender.sendMessage(
                miniMessage.deserialize("<green>Revived ${args[0]} to 1/2 heart.</green>")
            )

            sender.inventory.remove(lifeSteal.reviveItemModel)


        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String> {
        if (args.size == 1) return StringUtil.copyPartialMatches(
            args[0],
            mutableListOf("<name of player>"),
            mutableListOf()
        )
        return mutableListOf()
    }
}