package io.github.cloudate9.lifesteal.lootbox

import dev.triumphteam.gui.builder.item.ItemBuilder
import io.github.cloudate9.lifesteal.LifeSteal
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Chest
import org.bukkit.enchantments.Enchantment
import kotlin.random.Random

class LootBoxManager(private val lifeSteal: LifeSteal) {

    var lootBox: Chest? = null
    var lootBoxLoc: Location? = lifeSteal.config.getLocation("lootbox.location")

    /**
     * Generates a random location for the loot box
     *
     * @return the location of the loot box
     */
    fun createLootBox(world: World): Location {

        deteriorateLootBox() // Remove the old loot box if it exists

        var xCords = 0
        var zCords = 0

        while (xCords >= -75 && xCords <= 75) {
            xCords = Random.nextInt(-400, 400)
        }

        while (zCords >= -75 && zCords <= 75) {
            zCords = Random.nextInt(-400, 400)
        }

        lootBoxLoc = Location(world, xCords.toDouble(), 150.0, zCords.toDouble())

        lootBoxLoc!!.block.type = Material.CHEST
        lootBox = lootBoxLoc!!.block.state as Chest //Cast the state, not the block.


        lootBox!!.inventory.clear()

        lootBox!!.inventory.setItem(
            Random.nextInt(27), ItemBuilder.from(Material.ENCHANTED_BOOK)
                .enchant(Enchantment.values().random())
                .build()
        )

        when (Random.nextInt(3)) {
            0 -> lootBox!!.inventory.setItem(Random.nextInt(27), lifeSteal.heartFragmentModel)
            1 -> {
                for (i in 0..1) {
                    lootBox!!.inventory.setItem(Random.nextInt(27), lifeSteal.heartFragmentModel)
                }
            }
            2 -> lootBox!!.inventory.setItem(Random.nextInt(27), ItemBuilder.from(Material.ELYTRA).build())
        }

        return lootBoxLoc!!
    }

    private fun deteriorateLootBox() {
        if (lootBoxLoc?.block?.type == Material.CHEST) {
            lootBoxLoc?.block?.type = Material.AIR
        }
    }

    fun save() {
        lifeSteal.config.set("lootbox.location", lootBoxLoc)
        lifeSteal.config.options().parseComments()
        lifeSteal.saveConfig()
    }
}