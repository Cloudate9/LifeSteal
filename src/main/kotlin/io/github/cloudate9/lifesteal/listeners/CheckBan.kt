package io.github.cloudate9.lifesteal.listeners

import io.github.cloudate9.lifesteal.LifeSteal
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class CheckBan(private val lifeSteal: LifeSteal): Listener {

    @EventHandler
    fun checkBan(e: PlayerJoinEvent) {
        if (lifeSteal.config.getList("banned")!!.contains(e.player.uniqueId.toString())) {
            e.player.kick(Component.text("You ran out of lives! Get someone to revive you to rejoin!"))
        }
    }
}