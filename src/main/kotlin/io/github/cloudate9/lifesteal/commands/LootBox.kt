package io.github.cloudate9.lifesteal.commands

import io.github.cloudate9.lifesteal.lootbox.LootBoxManager
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

class LootBox(private val lootBoxManager: LootBoxManager, private val miniMessage: MiniMessage) : CommandExecutor,
    TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (args.isEmpty()) {
            sender.sendMessage(
                miniMessage.deserialize("<red>Incorrect usage: command is /lootbox new/status </red>")
            )
        }

        when (args[0].lowercase()) {
            "new" -> {
                if (!sender.hasPermission("lifesteal.lootbox.new")) {
                    sender.sendMessage(
                        miniMessage.deserialize("<red>You do not have permission to use this command!</red>")
                    )
                    return true
                }

                val lootBoxLoc = lootBoxManager.createLootBox(Bukkit.getWorlds().first())
                for (player in Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("lifesteal.lootbox.status"))
                        player.sendMessage(
                            miniMessage.deserialize(
                                "<green>A new loot box can be found at " +
                                        "${lootBoxLoc.x} ${lootBoxLoc.y} ${lootBoxLoc.z}!</green>"
                            )
                        )
                }

                sender.sendMessage(
                    miniMessage.deserialize("<green>Loot box generation successful!</green>")
                )

                return true
            }

            "status" -> {
                if (lootBoxManager.lootBoxLoc == null || lootBoxManager.lootBox == null ||
                    lootBoxManager.lootBox!!.inventory.isEmpty
                ) {
                    sender.sendMessage(
                        miniMessage.deserialize("<red>There is no loot box currently in the world!</red>")
                    )
                    return true
                }

                sender.sendMessage(
                    miniMessage.deserialize(
                        "<green>Loot box is at " +
                                "${lootBoxManager.lootBoxLoc!!.x} ${lootBoxManager.lootBoxLoc!!.y} ${lootBoxManager.lootBoxLoc!!.z}!</green>"
                    )
                )
                return true
            }
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
            mutableListOf("new", "status"),
            mutableListOf()
        )
        return mutableListOf()
    }
}