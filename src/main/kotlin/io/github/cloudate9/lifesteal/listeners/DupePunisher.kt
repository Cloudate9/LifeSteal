package io.github.cloudate9.lifesteal.listeners

import io.github.cloudate9.lifesteal.LifeSteal
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class DupePunisher(private val lifeSteal: LifeSteal, private val miniMessage: MiniMessage) : Listener {

    @EventHandler
    fun duperJoin(e: PlayerJoinEvent) {
        val dupers = lifeSteal.config.getConfigurationSection("dupers")?.getKeys(false) ?: return
        if (dupers.contains(e.player.uniqueId.toString())) {

            var healthDuped = lifeSteal.config.getInt("dupers." + e.player.uniqueId.toString() + ".heartsDuped") * 2
            val payedBack: Int

            val dupedMaxHealth = e.player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue

            if (dupedMaxHealth > healthDuped) {
                payedBack = healthDuped

                e.player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue -= healthDuped
                lifeSteal.config.set("dupers." + e.player.uniqueId.toString(), null)

                healthDuped = 0

            } else {
                payedBack = (dupedMaxHealth.toInt() - 2)

                e.player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue -= (dupedMaxHealth - 2)
                lifeSteal.config.set("dupers." + e.player.uniqueId.toString() + ".heartsDuped",
                    //dupedMaxHealth - 2 so that the player does not have 0 hearts, then divide by 2 to get hearts from health.
                    (healthDuped - (dupedMaxHealth - 2)) / 2
                )

                healthDuped -= payedBack
            }

            lifeSteal.saveConfig()

            e.player.sendMessage(miniMessage.deserialize("<red>You payed back ${payedBack / 2} worth of duped hearts ." +
                    "You have ${healthDuped / 2} more hearts to return."))
            return
        }
    }
}