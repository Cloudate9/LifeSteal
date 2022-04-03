package io.github.cloudate9.lifesteal.listeners

import io.github.cloudate9.lifesteal.LifeSteal
import net.kyori.adventure.text.Component
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent

class CheckBan(private val lifeSteal: LifeSteal) : Listener {

    @EventHandler
    fun checkBan(e: AsyncPlayerPreLoginEvent) {
        if (lifeSteal.config.getList("banned")!!.contains(e.uniqueId.toString())) {
            e.disallow(
                AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                Component.text("You ran out of lives! Get someone to revive you to rejoin!")
            )
        }
    }

    @EventHandler
    fun ensureMinimumHealth(e: PlayerJoinEvent) {
        if (e.player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue < 1.0)
            e.player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = 1.0
    }
}