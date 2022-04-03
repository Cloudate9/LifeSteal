package io.github.cloudate9.lifesteal.listeners

import io.github.cloudate9.lifesteal.LifeSteal
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent

class DeductHearts(private val lifeSteal: LifeSteal, private val miniMessage: MiniMessage) : Listener {

    private fun punish(deadPlayer: Player, killer: Player?) {
        val deadPlayerHearts = deadPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)
        if (deadPlayerHearts!!.baseValue < 2.0) {
            val bannedList = lifeSteal.config.getList("banned") as MutableList<String>

            if (!bannedList.contains(deadPlayer.name)) {
                bannedList.add(deadPlayer.uniqueId.toString())
                lifeSteal.config.set("banned", bannedList)
                lifeSteal.saveConfig()
            }
            Bukkit.broadcast(
                miniMessage.deserialize("<red>${deadPlayer.name} ran out of hearts and was banned from the server!")
            )
        } else deadPlayerHearts.baseValue -= 2.0

        if (killer != null && killer != deadPlayer)
            killer.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue += 2.0
    }

    //Kick the player in respawn instead of death, so that their logon point won't be the location they were previously at.
    @EventHandler
    fun attemptedRespawn(e: PlayerRespawnEvent) {
        if (lifeSteal.config.getList("banned")!!.contains(e.player.uniqueId.toString()))
            e.player.kick(Component.text("You ran out of lives! Get someone to revive you to rejoin!"))
    }

    @EventHandler
    fun onDeath(e: PlayerDeathEvent) {
        punish(e.entity, e.entity.killer)
    }

}