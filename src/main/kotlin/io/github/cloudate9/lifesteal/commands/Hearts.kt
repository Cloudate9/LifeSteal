package io.github.cloudate9.lifesteal.commands

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

class Hearts(private val miniMessage: MiniMessage) : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(
                miniMessage.deserialize("<red>Incorrect usage: command is /hearts add/sub/set <player> <health points></red>")
            )
            return true
        }

        if (sender is Player && !sender.hasPermission("lifesteal.admin")) {
            sender.sendMessage(
                miniMessage.deserialize("<red>You do not have permission to use this command!</red>")
            )

            return true
        }

        val playerToModify = Bukkit.getPlayer(args[1])
        if (playerToModify == null) {
            sender.sendMessage(
                miniMessage.deserialize("<red>${args[1]} needs to be online!</red>")
            )
            return true
        }

        when (args[0].lowercase()) {
            "add" -> {
                val amount = if (args[2].toInt() < 1) 1 else args[2].toInt()
                playerToModify.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue += amount
                playerToModify.sendMessage(
                    miniMessage.deserialize("<green>Your max health increased by ${amount / 2} hearts!</green>")
                )
                sender.sendMessage(
                    miniMessage.deserialize("<green>Increased max health of ${playerToModify.name} by $amount.</green>")
                )
            }

            "set" -> {
                val amount = if (args[2].toInt() < 1) 1 else args[2].toInt()

                playerToModify.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = amount.toDouble()
                playerToModify.sendMessage(
                    miniMessage.deserialize("<green>Your max health has been set to ${amount / 2} hearts!</green>")
                )
                sender.sendMessage(
                    miniMessage.deserialize("<green>Set max health of ${playerToModify.name} to $amount.</green>")
                )
            }

            "sub" -> {
                val amount = args[2].toInt()
                val origHealth = playerToModify.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue
                if (amount >= origHealth) {
                    sender.sendMessage(
                        miniMessage.deserialize(
                            "<red>Cannot decrease max health of ${playerToModify.name} by $amount, " +
                                    "as they only have $origHealth health points.</red>"
                        )
                    )
                    return true
                }
                playerToModify.sendMessage(
                    miniMessage.deserialize("<green>Your max health has decreased by ${amount / 2} hearts!</green>")
                )
                sender.sendMessage(
                    miniMessage.deserialize("<green>Decreased max health of ${playerToModify.name} by $amount.</green>")
                )
            }

            else -> {
                sender.sendMessage(
                    miniMessage.deserialize("<red>Incorrect usage: command is /hearts add/sub/set <health points>")
                )
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
        when (args.size) {
            1 -> return StringUtil.copyPartialMatches(args[0], mutableListOf("add", "set", "sub"), mutableListOf())
            2 -> return StringUtil.copyPartialMatches(
                args[1],
                Bukkit.getOnlinePlayers().map { it.name },
                mutableListOf()
            )
            3 -> StringUtil.copyPartialMatches(args[2], mutableListOf("<Number of hearts>"), mutableListOf())
            else -> return mutableListOf()
        }
        return mutableListOf()
    }
}
