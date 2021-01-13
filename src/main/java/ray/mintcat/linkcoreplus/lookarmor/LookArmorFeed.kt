package ray.mintcat.linkcoreplus.lookarmor

import io.izzel.taboolib.TabooLibAPI
import io.izzel.taboolib.util.item.inventory.MenuBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import ray.mintcat.linkcoreplus.LinkCorePlus
import ray.mintcat.linkcoreplus.invslot.InvSlotFeed
import ray.mintcat.linkcoreplus.lookarmor.LookArmorFeed.papi

object LookArmorFeed {


    private val config = LinkCorePlus.lookplayer

    val playerMap = mutableMapOf<Player,Player>()

    fun openMenu(player: Player, target: Player) {
        val menu = MenuBuilder.builder(LinkCorePlus.plugin)
        menu.title(config.getStringColored("Menu.Title", "查看玩家").papi(target))
        menu.rows(config.getInt("Menu.Rows", 1))
        menu.build { inv ->
            config.getConfigurationSection("Slots").getKeys(false).forEach {
                inv.setItem(it.toInt(),itemBuilder(it.toInt(),target))
            }
        }
        menu.event {
            it.isCancelled = true
            if (config.getStringList("Slots.${it.rawSlot}.action") != null){
                TabooLibAPI.getPluginBridge().setPlaceholders(player, config.getStringListColored("Slots.${it.rawSlot}.action")).forEach { action ->
                    InvSlotFeed.runAction(action,player)
                }
            }

        }
        menu.open(player)
    }

    private fun itemBuilder(slot: Int, player: Player):ItemStack{
        return when (val type = config.getStringColored("Slots.$slot.item.type"," ").papi(player)){
            "MainHand" -> player.inventory.itemInMainHand ?: ItemStack(Material.AIR)
            "OffHand" -> player.inventory.itemInOffHand ?: ItemStack(Material.AIR)
            "Boots" -> player.inventory.boots ?: ItemStack(Material.AIR)
            "Helmet" -> player.inventory.helmet ?: ItemStack(Material.AIR)
            "Leggings" -> player.inventory.leggings ?: ItemStack(Material.AIR)
            "Chest" -> player.inventory.chestplate ?: ItemStack(Material.AIR)
            else -> {
                val name = config.getStringColored("Slots.$slot.item.name"," ").papi(player)
                val lore = TabooLibAPI.getPluginBridge().setPlaceholders(player, config.getStringListColored("Slots.$slot.item.lore"))
                val amount = config.getStringColored("Slots.$slot.item.amount","1").papi(player).toInt()
                val damage = config.getStringColored("Slots.$slot.item.damage","0").papi(player).toInt()
                InvSlotFeed.toItem(name, lore, type, amount, damage)
            }
        }
    }

    private fun String.papi(player: Player): String {
        return TabooLibAPI.getPluginBridge().setPlaceholders(player, this)
    }


}
