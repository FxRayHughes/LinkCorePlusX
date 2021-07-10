package ray.mintcat.linkcoreplus.strengthen

import io.izzel.taboolib.TabooLibAPI
import io.izzel.taboolib.cronus.CronusUtils
import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.item.Items
import io.izzel.taboolib.util.item.inventory.MenuBuilder
import io.izzel.taboolib.util.lite.Materials
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import ray.mintcat.linkcoreplus.LinkCorePlus
import ray.mintcat.linkcoreplus.invslot.InvSlotFeed
import ray.mintcat.linkcoreplus.lookarmor.LookArmorFeed
import ray.mintcat.linkcoreplus.lookarmor.LookArmorFeed.papi
import ray.mintcat.linkcoreplus.strengthen.StrengThenFeed.getLevel
import ray.mintcat.linkcoreplus.utils.Helper
import ray.mintcat.wizardfix.Data

object StrengThen : Helper {
    

    private val config = LinkCorePlus.streng

    fun openGUI(player: Player) {
        val item = player.inventory.itemInMainHand
        if (!StrengThenFeed.canStrengThen(item)) {
            player.error("该物品无法强化")
            return
        }
        val menu = MenuBuilder.builder(LinkCorePlus.plugin)
        menu.title(config.getStringColored("Menu.Title", "查看玩家").papi(player))
        menu.rows(config.getInt("Menu.Rows", 3))
        menu.build { inv ->
            config.getConfigurationSection("Slots").getKeys(false).forEach {
                inv.setItem(it.toInt(),itemBuilder(it.toInt(), player))
            }
        }
        menu.event {
            it.isCancelled = true
            if (config.getStringList("Slots.${it.rawSlot}.action") != null){
                InvSlotFeed.eval(player, config.getStringList("Slots.${it.rawSlot}.action"))
            }
            when (config.getString("Slots.${it.rawSlot}.about")) {
                "开始强化" -> {
                    StrengThenFeed.run(item, player, item.getLevel() + 1)
                    openGUI(player)
                }
                "放入强化石" -> {
                    MenuBuilder.builder(LinkCorePlus.plugin)
                        .title("请放入物品")
                        .rows(1)
                        .items("####@####")
                        .put('#', ItemBuilder(Materials.BLACK_STAINED_GLASS_PANE.parseItem()).name("§f").build())
                        .put('@', ItemStack(Material.AIR))
                        .event { event ->
                            if (event.slot == '#') {
                                event.isCancelled = true
                            }
                        }.close { event ->
                            val items = event.inventory.getItem(4)!!
                            val types = config.getString("gem")
                            if (Items.isNull(items) || !Items.hasLore(items, types)) {
                                CronusUtils.addItem(player, items)
                                player.error("物品类型不符合!")
                                return@close
                            }
                            var values: Double = 0.0
                            items.itemMeta.lore?.forEach { key ->
                                if (key.contains(types!!)) {
                                    val lores = ChatColor.stripColor(key)?.toLowerCase() ?: "0"
                                    val value = (lores.replace("[^0-9.]".toRegex(), "").toDouble()) * items.amount
                                    Data(player).edit("强化石系数", "+", value.toString())
                                    values += value
                                }
                            }
                            player.info("增加了 &f${values}&7 点系数")
                        }.open(player)
                }
            }
        }
        menu.open(player)
    }

    fun itemBuilder(slot: Int, player: Player):ItemStack{
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
}