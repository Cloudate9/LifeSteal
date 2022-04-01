package io.github.cloudate9.lifesteal.listeners

import com.github.sirblobman.combatlogx.api.event.PlayerPunishEvent
import io.github.cloudate9.lifesteal.LifeSteal
import net.kyori.adventure.text.Component
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class KilledByPlayer(private val lifeSteal: LifeSteal) : Listener {

    private fun punish(deadPlayer: Player, killer: Player?) {
        val deadPlayerHearts = deadPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)
        if (deadPlayerHearts!!.baseValue < 2.0) {
            val bannedList = lifeSteal.config.getList("banned") as MutableList<String>

            bannedList.add(deadPlayer.uniqueId.toString())
            lifeSteal.config.set("banned", bannedList)
            lifeSteal.saveConfig()
            deadPlayer.kick(Component.text("You ran out of lives! Get someone to revive you to rejoin!"))
        } else deadPlayerHearts.baseValue -= 2.0

        if (killer != null && killer != deadPlayer)
            killer.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue += 2.0
    }

    @EventHandler
    fun combatLogged(e: PlayerPunishEvent) {
        punish(e.player, e.previousEnemy as Player?)
    }

    @EventHandler
    fun onDeath(e: PlayerDeathEvent) {
        punish(e.entity, e.entity.killer)
    }

}