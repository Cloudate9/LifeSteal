package io.github.cloudate9.lifesteal.listeners

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class DisableElytra(private val plugin: JavaPlugin) : Listener {

    @EventHandler
    fun playerEquipElytra(e: PlayerArmorChangeEvent) {
        if (e.newItem?.type == Material.ELYTRA) {
            object : BukkitRunnable() {
                override fun run() {
                    val elytra = e.player.inventory.chestplate ?: return
                    e.player.inventory.chestplate = null
                    if (e.player.inventory.firstEmpty() != -1) {
                        e.player.inventory.addItem(elytra)
                    } else {
                        e.player.world.dropItemNaturally(e.player.location, elytra)
                    }
                }
            }.runTaskLater(plugin, 1)
        }
    }
}
