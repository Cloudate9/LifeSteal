package io.github.cloudate9.lifesteal.listeners

import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UnlockRecipe: Listener {

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if (!e.player.hasDiscoveredRecipe(NamespacedKey.minecraft("heart")))
            e.player.discoverRecipe(NamespacedKey.minecraft("heart"))

        if (!e.player.hasDiscoveredRecipe(NamespacedKey.minecraft("heart_fragment")))
            e.player.discoverRecipe(NamespacedKey.minecraft("heart_fragment"))

        if (!e.player.hasDiscoveredRecipe(NamespacedKey.minecraft("revive")))
            e.player.discoverRecipe(NamespacedKey.minecraft("revive"))
    }
}