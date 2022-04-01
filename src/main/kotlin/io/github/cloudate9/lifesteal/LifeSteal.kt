package io.github.cloudate9.lifesteal

import io.github.cloudate9.lifesteal.commands.Restore
import io.github.cloudate9.lifesteal.commands.Revive
import io.github.cloudate9.lifesteal.commands.Withdraw
import io.github.cloudate9.lifesteal.listeners.*
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin
import java.net.InetSocketAddress

class LifeSteal: JavaPlugin() {

    companion object {
        val ipList = mutableListOf<InetSocketAddress>()
        val miniMessage = MiniMessage.miniMessage()
        lateinit var heartFragmentModel: ItemStack
        lateinit var heartItemModel: ItemStack
        lateinit var reviveItemModel: ItemStack
    }

    override fun onEnable() {
        heartFragment()
        heartItem()
        reviveItem()

        saveDefaultConfig()

        val pluginManager = Bukkit.getPluginManager()
        pluginManager.registerEvents(CheckBan(this), this)
        pluginManager.registerEvents(ClickHeart(), this)
        pluginManager.registerEvents(KilledByPlayer(this), this)
        pluginManager.registerEvents(NoSpawnPVP(miniMessage), this)
        pluginManager.registerEvents(TabEdit(miniMessage), this)
        pluginManager.registerEvents(UnlockRecipe(), this)

        getCommand("restore")?.setExecutor(Restore())
        getCommand("revive")?.setExecutor(Revive(this))
        getCommand("withdraw")?.setExecutor(Withdraw())
    }

    private fun heartFragment() {

        val fragment = ItemStack(Material.CARROT_ON_A_STICK)
        val fragmentMeta = fragment.itemMeta

        fragmentMeta.lore(
            listOf(
                miniMessage.deserialize("<light_purple>Use four of these to craft a heart!</light_purple>")
            )
        )

        fragmentMeta.displayName(
            miniMessage.deserialize("<red>Heart Fragment</red>")
        )

        fragmentMeta.setCustomModelData(25001)

        fragment.itemMeta = fragmentMeta

        val fragmentRecipe = ShapedRecipe(NamespacedKey.minecraft("heart_fragment"), fragment)
        fragmentRecipe.shape("ODO", "DTD", "ODO")

        fragmentRecipe.setIngredient('O', Material.OBSIDIAN)
        fragmentRecipe.setIngredient('D', Material.DIAMOND_BLOCK)
        fragmentRecipe.setIngredient('T', Material.TOTEM_OF_UNDYING)
        Bukkit.getServer().addRecipe(fragmentRecipe)

        heartFragmentModel = fragment
    }

    private fun heartItem() {

        val heart = ItemStack(Material.WARPED_FUNGUS_ON_A_STICK)
        val heartMeta = heart.itemMeta

        heartMeta.lore(
            listOf(
                miniMessage.deserialize("<light_purple>Use one of these to gain a heart!</light_purple>")
            )
        )

        heartMeta.displayName(
            miniMessage.deserialize("<red>Heart</red>")
        )

        heartMeta.setCustomModelData(25002)

        heart.itemMeta = heartMeta

        val heartRecipe = ShapedRecipe(NamespacedKey.minecraft("heart"), heart)
        heartRecipe.shape("FDF", "DED", "FDF")

        heartRecipe.setIngredient('F', heartFragmentModel)
        heartRecipe.setIngredient('D', Material.DIAMOND_BLOCK)
        heartRecipe.setIngredient('E', Material.ELYTRA)
        Bukkit.getServer().addRecipe(heartRecipe)

        heartItemModel = heart
    }

    private fun reviveItem() {
        val revive = ItemStack(Material.FIREWORK_STAR)
        val reviveMeta = revive.itemMeta

        reviveMeta.lore(
            listOf(
                miniMessage.deserialize("<light_purple>Use /revive with one of these to revive a player!</light_purple>")
            )
        )

        reviveMeta.displayName(
            miniMessage.deserialize("<red>Reviver</red>")
        )

        reviveMeta.setCustomModelData(25003)

        revive.itemMeta = reviveMeta

        val reviveRecipe = ShapedRecipe(NamespacedKey.minecraft("revive"), revive)
        reviveRecipe.shape("HNH", "NSN", "HNH")

        reviveRecipe.setIngredient('H', heartItemModel)
        reviveRecipe.setIngredient('N', Material.NETHERITE_INGOT)
        reviveRecipe.setIngredient('S', Material.NETHER_STAR)
        Bukkit.getServer().addRecipe(reviveRecipe)

        reviveItemModel = revive
    }
}