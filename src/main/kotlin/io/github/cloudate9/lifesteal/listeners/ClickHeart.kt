package io.github.cloudate9.lifesteal.listeners

import io.github.cloudate9.lifesteal.LifeSteal
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class ClickHeart(private val lifeSteal: LifeSteal, private val miniMessage: MiniMessage) : Listener {

    @EventHandler
    fun rightClick(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        if (!(e.item?.isSimilar(lifeSteal.heartItemModel) ?: return)) return

        val dupers = lifeSteal.config.getConfigurationSection("dupers")?.getKeys(false) ?: listOf<String>()
        if (dupers.contains(e.player.uniqueId.toString())) {

            val heartsDuped = lifeSteal.config.getInt("dupers." + e.player.uniqueId.toString() + ".heartsDuped")
            if (heartsDuped >= e.item!!.amount) {
                e.player.sendMessage(miniMessage.deserialize("<red>You payed back ${e.item!!.amount} worth of duped hearts." +
                        "You have ${heartsDuped - e.item!!.amount} more hearts to return."))
                lifeSteal.config.set("dupers." + e.player.uniqueId.toString() + ".heartsDuped", heartsDuped - e.item!!.amount)
                e.item!!.amount = 0
                lifeSteal.saveConfig()
                return
            } else {
                e.player.sendMessage(miniMessage.deserialize("<red>You payed back $heartsDuped worth of duped hearts. " +
                        "You have ${e.item!!.amount - heartsDuped} more hearts to return."))
                lifeSteal.config.set("dupers." + e.player.uniqueId.toString(), null)
                e.item!!.amount = e.item!!.amount - heartsDuped
                lifeSteal.saveConfig()
            }

        }

        var totalIncHealth = 0

        for (amount in 1..e.item!!.amount) {
            val currentMaxHealth = e.player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue
            val healthToInc = if (currentMaxHealth == 1.0) 1.0 else 2.0
            e.player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue += healthToInc
            totalIncHealth += healthToInc.toInt() //We are always working with ints here anyway.
            --e.item!!.amount
        }

        e.player.sendMessage(
            miniMessage.deserialize("<green>Your gained $totalIncHealth health!</green>")
        )
    }

}