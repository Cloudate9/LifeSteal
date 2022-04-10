package io.github.cloudate9.lifesteal.listeners

import io.github.cloudate9.lifesteal.LifeSteal
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent

class CheckIllegalHearts(private val lifeSteal: LifeSteal): Listener {

    @EventHandler
    fun inventoryOpen(e: InventoryOpenEvent) {
        for (item in e.inventory) {
            when (item?.itemMeta?.customModelData ?: return) {
                25001 -> {
                    if (!item.isSimilar(lifeSteal.heartFragmentModel)) {
                        item.amount = 0
                    }
                }
                25002 -> {
                    if (!item.isSimilar(lifeSteal.heartItemModel)) {
                        item.amount = 0
                    }
                }
                25003 -> {
                    if (!item.isSimilar(lifeSteal.reviveItemModel)) {
                        item.amount = 0
                    }
                }
            }
        }
    }

}