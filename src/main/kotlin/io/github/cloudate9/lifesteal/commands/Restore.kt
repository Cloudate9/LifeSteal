package io.github.cloudate9.lifesteal.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Restore : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(
                Component.text("Incorrect usage: command is /restore (player name)")
                    .color(TextColor.color(Integer.parseInt("FF5555", 16)))
            )
            return true
        }

        if ((sender !is Player || sender.hasPermission("lifesteal.admin"))) {

            val playerToRestore = Bukkit.getPlayer(args[0])
            if (playerToRestore == null) {
                sender.sendMessage(
                    Component.text("${args[0]} needs to be online!")
                        .color(TextColor.color(Integer.parseInt("FF5555", 16)))
                )
                return true
            }

            sender.sendMessage(
                Component.text("Restored ${args[0]}'s max health to 10 hearts.")
                    .color(TextColor.color(Integer.parseInt("55FF55", 16)))
            )


            playerToRestore.sendMessage(
                Component.text(
                    "Your max health has been restored to 10 hearts."
                ).color(TextColor.color(Integer.parseInt("55FF55", 16)))
            )


            playerToRestore.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = 20.0
            return true
        }

        sender.sendMessage(
            Component.text("Incorrect usage: command is /withdraw (number of hearts)")
                .color(TextColor.color(Integer.parseInt("FF5555", 16)))
        )

        return true
    }


}