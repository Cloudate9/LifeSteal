package io.github.cloudate9.lifesteal.listeners

import io.github.cloudate9.lifesteal.LifeSteal
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class ClickHeart: Listener {

    @EventHandler
    fun rightClick(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        if (e.item != LifeSteal.heartItemModel) return

        e.item!!.amount = 0

        val currentMaxHealth = e.player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue
        val healthToInc = if (currentMaxHealth == 1.0) 1.0 else 2.0
        e.player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue += healthToInc

        e.player.sendMessage(
            Component.text("Your gained " + healthToInc + "health!")
                .color(TextColor.color(Integer.parseInt("55FF55", 16)))
        )
    }
}