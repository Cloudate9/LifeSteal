package io.github.cloudate9.lifesteal.listeners

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent

class TabEdit(private val miniMessage: MiniMessage) : Listener {

    @EventHandler
    fun playerJoin(e: PlayerJoinEvent) {
        e.player.playerListName(
            miniMessage.deserialize(
                "<red>" + e.player.health.toInt() + "❤  </red>" + e.player.name
            )
        )
    }

    @EventHandler
    fun gainHealth(e: EntityRegainHealthEvent) {
        if (e.entity is Player) {
            (e.entity as Player).playerListName(
                miniMessage.deserialize(
                    "<red>" + (e.entity as Player).health.toInt() +
                            "❤  </red>" + e.entity.name
                )
            )
        }
    }

    @EventHandler
    fun loseHealth(e: EntityDamageEvent) {
        if (e.entity is Player) {
            (e.entity as Player).playerListName(
                miniMessage.deserialize(
                    "<red>" + (e.entity as Player).health.toInt() +
                            "❤  </red>" + e.entity.name
                )
            )

        }
    }

    @EventHandler
    fun completeRespawn(e: PlayerRespawnEvent) {
        e.player.playerListName(
            miniMessage.deserialize(
                "<red>" + e.player.health.toInt() + "❤  </red>" + e.player.name
            )
        )
    }

}