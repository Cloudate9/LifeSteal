package io.github.cloudate9.lifesteal

import dev.triumphteam.gui.builder.item.ItemBuilder
import io.github.cloudate9.lifesteal.commands.Hearts
import io.github.cloudate9.lifesteal.commands.LootBox
import io.github.cloudate9.lifesteal.commands.Revive
import io.github.cloudate9.lifesteal.commands.Withdraw
import io.github.cloudate9.lifesteal.listeners.*
import io.github.cloudate9.lifesteal.lootbox.LootBoxManager
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin
import java.net.InetSocketAddress

class LifeSteal : JavaPlugin() {

    private lateinit var lootBoxManager: LootBoxManager
    private val miniMessage = MiniMessage.miniMessage()

    val heartFragmentModel =
        ItemBuilder.from(Material.CARROT_ON_A_STICK)
            .name(miniMessage.deserialize("<red>Heart Fragment</red>"))
            .lore(miniMessage.deserialize("<light_purple>Use four of these to craft a heart!</light_purple>"))
            .model(25001).build()

    val heartItemModel =
        ItemBuilder.from(Material.RED_DYE)
            .name(miniMessage.deserialize("<red>Heart</red>"))
            .lore(miniMessage.deserialize("<light_purple>Use one of these to gain a heart!</light_purple>"))
            .model(25002).build()

    val reviveItemModel =
        ItemBuilder.from(Material.FIREWORK_STAR)
            .name(miniMessage.deserialize("<red>Reviver</red>"))
            .lore(miniMessage.deserialize("<light_purple>Use /revive with one of these to revive a player!</light_purple>"))
            .model(25003).build()

    companion object {
        val ipList = mutableListOf<InetSocketAddress>()
    }

    override fun onEnable() {
        heartFragment()
        heartItem()
        reviveItem()

        saveDefaultConfig()

        lootBoxManager = LootBoxManager(this)
        val pluginManager = Bukkit.getPluginManager()

        pluginManager.registerEvents(CheckBan(this), this)
        pluginManager.registerEvents(ClickHeart(this, miniMessage), this)
        pluginManager.registerEvents(DeductHearts(this, miniMessage), this)
        pluginManager.registerEvents(DisableElytra(this, miniMessage), this)
        pluginManager.registerEvents(DupePunisher(this, miniMessage), this)
        pluginManager.registerEvents(NoSpawnPVP(miniMessage), this)
        pluginManager.registerEvents(TabEdit(miniMessage), this)
        pluginManager.registerEvents(UnlockRecipe(), this)

        getCommand("hearts")?.setExecutor(Hearts(miniMessage))
        getCommand("lootbox")?.setExecutor(LootBox(lootBoxManager, miniMessage))
        getCommand("revive")?.setExecutor(Revive(this, miniMessage))
        getCommand("withdraw")?.setExecutor(Withdraw(this, miniMessage))

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, {
            Bukkit.getOnlinePlayers().forEach {
                it.playerListName(
                    miniMessage.deserialize(
                        "<red>" + it.health.toInt() +
                                "❤  </red>" + it.name
                    )
                )
            }
        }, 0, 5 * 20)
    }

    override fun onDisable() {
        lootBoxManager.save()
    }

    private fun heartFragment() {
        val fragmentRecipe = ShapedRecipe(NamespacedKey.minecraft("heart_fragment"), heartFragmentModel)
        fragmentRecipe.shape("ODO", "DTD", "ODO")

        fragmentRecipe.setIngredient('O', Material.OBSIDIAN)
        fragmentRecipe.setIngredient('D', Material.DIAMOND_BLOCK)
        fragmentRecipe.setIngredient('T', Material.TOTEM_OF_UNDYING)
        Bukkit.getServer().addRecipe(fragmentRecipe)
    }

    private fun heartItem() {
        val heartRecipe = ShapedRecipe(NamespacedKey.minecraft("heart"), heartItemModel)
        heartRecipe.shape("FDF", "DED", "FDF")

        heartRecipe.setIngredient('F', heartFragmentModel)
        heartRecipe.setIngredient('D', Material.DIAMOND_BLOCK)
        heartRecipe.setIngredient('E', Material.ELYTRA)
        Bukkit.getServer().addRecipe(heartRecipe)
    }

    private fun reviveItem() {
        val reviveRecipe = ShapedRecipe(NamespacedKey.minecraft("revive"), reviveItemModel)
        reviveRecipe.shape("HNH", "NSN", "HNH")

        reviveRecipe.setIngredient('H', heartItemModel)
        reviveRecipe.setIngredient('N', Material.NETHERITE_INGOT)
        reviveRecipe.setIngredient('S', Material.NETHER_STAR)
        Bukkit.getServer().addRecipe(reviveRecipe)
    }
}