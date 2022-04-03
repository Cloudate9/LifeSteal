package io.github.cloudate9.lifesteal.commands

import io.github.cloudate9.lifesteal.LifeSteal
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil


class Withdraw(private val lifeSteal: LifeSteal, private val miniMessage: MiniMessage) : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {

        lifeSteal.reloadConfig()

        if (sender !is Player) {

            sender.sendMessage(
                miniMessage.deserialize("<red>Only players can use /withdraw</red>")
            )

            return true
        }

        if (sender.hasPermission("lifesteal.withdraw")) {

            val heartsToWithdraw = if (args.isEmpty()) 1 else if (args[0].toInt() < 1) 1 else args[0].toInt()
            if (sender.health < 2 || sender.health <= (heartsToWithdraw * 2)) {

                // -2 cause ensure player always has 1 heart. heartsToWithdraw * 2 because we check health points, not hearts.
                sender.sendMessage(
                    miniMessage.deserialize("<red>You don't have enough hearts!</red>")
                )

                return true
            }

            var emptySlots = 0
            for (itemStack in sender.inventory.contents!!) {
                if (itemStack == null || itemStack.type == Material.AIR) {
                    emptySlots += lifeSteal.heartItemModel.maxStackSize
                }
            }

            if (emptySlots < heartsToWithdraw) {
                sender.sendMessage(
                    miniMessage.deserialize("<red>You don't have enough space in your inventory!</red>")
                )
                return true
            }

            sender.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue -= heartsToWithdraw * 2
            sender.health -= heartsToWithdraw * 2
            for (number in 1..heartsToWithdraw) sender.inventory.addItem(lifeSteal.heartItemModel)


            sender.sendMessage(
                miniMessage.deserialize("<green>Your withdrew $heartsToWithdraw hearts!</green>")
            )

            return true
        }
        sender.sendMessage(
            miniMessage.deserialize("<green>Incorrect usage: command is /withdraw <number of hearts></green>")
        )

        return true

    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        if (args.size == 1) return StringUtil.copyPartialMatches(args[0], mutableListOf("<Number of hearts>"), mutableListOf())
        return mutableListOf()
    }
}