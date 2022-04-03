package io.github.cloudate9.lifesteal.listeners

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class NoSpawnPVP(private val miniMessage: MiniMessage) : Listener {

    @EventHandler
    fun pvp(e: EntityDamageByEntityEvent) {
        if (e.entity !is Player || e.damager !is Player) return
        if (e.entity.world.environment != World.Environment.NORMAL || e.damager.world.environment != World.Environment.NORMAL) return
        if ((e.entity.location.x < 75.0 && e.entity.location.x > -75.0) ||
            (e.entity.location.z > 75.0 && e.entity.location.z < -75.0)
        ) {
            e.isCancelled = true
            val message = miniMessage.deserialize("<red>No fighting at spawn!</red>")
            e.entity.sendMessage(message)
            e.damager.sendMessage(message)
        }
    }

}