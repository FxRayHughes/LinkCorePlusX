package ray.mintcat.linkcoreplus.strengthen

import io.izzel.taboolib.TabooLibAPI
import io.izzel.taboolib.kotlin.colored
import io.izzel.taboolib.util.Features
import io.izzel.taboolib.util.item.Items
import io.lumine.xikage.mythicmobs.MythicMobs
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import ray.mintcat.linkcoreplus.LinkCorePlus
import ray.mintcat.linkcoreplus.utils.Helper
import ray.mintcat.linkcoreplus.utils.Utils
import ray.mintcat.wizardfix.Data

object StrengThenFeed : Helper {

    val config = LinkCorePlus.streng

    fun upWeight(player: Player, add: Int): Int {
        val basics = config.getInt("levels.${add}.basics")
        return basics
    }


    fun Player.getItemAmount(itemStack: ItemStack): Int {
        var i = 0
        Items.takeItem(this.inventory, {
            if (it.isSimilar(itemStack)) {
                i += it.amount
            }
            false
        }, 999)
        return i
    }

    fun run(itemStack: ItemStack, player: Player, add: Int) {
        if (!canStrengThen(itemStack)) {
            player.error("该物品无法强化")
            return
        }
        val conf = config.getConfigurationSection("levels.${add}") ?: return
        val level = itemStack.getLevel()
        val basics = conf.getInt("basics")
        val hand = Data(player).get("强化保护符", "0.0").toDouble()
        val downS = conf.getString("down")?.split("-") ?: listOf("0", "0")
        val down = ((downS[0].toInt())..(downS[1].toInt())).randomOrNull() ?: 0

        //判断物品
        // 强化石 | 20
        val items = conf.getStringList("items").map { item ->
            val itema = MythicMobs.inst().itemManager.getItemStack(item.split(" | ")[0])
            player.getItemAmount(itema) >= (item.split(" | ")[1]).toInt()
        }
        if (items.contains(false)){
            player.error("材料不足无法强化")
            return
        }
        conf.getStringList("items").map { item ->
            val itema = MythicMobs.inst().itemManager.getItemStack(item.split(" | ")[0])
            Items.checkItem(player, itema, (item.split(" | ")[1]).toInt(), true)
        }

        Utils.check(player, conf.getStringList("check")).thenAccept {
            Data(player).edit("强化石系数", "=", "0.0")
            val can = basics >= (0..100).random()
            if (it && can) {
                setLevelName(itemStack, add)
                setLevelLore(itemStack, add)
                Utils.eval(player, conf.getStringList("action.success").map { actions ->
                    TabooLibAPI.getPluginBridge().setPlaceholders(player, actions)
                        .replace("{down}", down.toString())
                        .replace("{item}", Items.getName(itemStack))
                        .replace("{level}", itemStack.getLevel().toString())
                })
            } else if (hand != 0.0 && down == 0) {
                Utils.eval(player, conf.getStringList("action.fail").map { actions ->
                    TabooLibAPI.getPluginBridge().setPlaceholders(player, actions)
                        .replace("{down}", "0")
                        .replace("{item}", Items.getName(itemStack))
                        .replace("{level}", itemStack.getLevel().toString())
                })
                Data(player).edit("强化保护符", "-", "1")
            } else {
                if (level - down != 0) {
                    setLevelName(itemStack, level - down)
                    setLevelLore(itemStack, level - down)
                } else {
                    setLevelName(itemStack, 0)
                    setLevelLore(itemStack, 0)
                }
                Utils.eval(player, conf.getStringList("action.fail").map { actions ->
                    TabooLibAPI.getPluginBridge().setPlaceholders(player, actions)
                        .replace("{down}", down.toString())
                        .replace("{item}", Items.getName(itemStack))
                        .replace("{level}", itemStack.getLevel().toString())
                })
            }
        }
    }

    fun canStrengThen(itemStack: ItemStack): Boolean {
        val info = config.getStringList("info")
        return info.map { Items.hasLore(itemStack, it) }.contains(true)
    }

    fun setLevelLore(itemStack: ItemStack, new: Int) {
        val attrs = config.getStringList("attribute")
        val lore = itemStack.itemMeta.lore ?: listOf()
        for (attr in attrs) {
            if (!Items.hasLore(itemStack, attr)) {
                continue
            }
            for (len in lore) {
                if (!len.contains(attr)) {
                    continue
                }
                //这行就是属性
                val args = len.replace(")", " (").split(" (")
                val newLore = config.getString("lore")!!
                    .replace("{lore}", args[0])
                    .replace(" )", ")")
                    .replace("{add}", config.getString("add")!!
                        .replace("{add}", new.getAdd()))
                Items.replaceLore(itemStack, len, newLore)
            }
        }
    }

    fun Int.getAdd(): String {
        return config.getString("levels.${this}.add") ?: "0"
    }

    fun setLevelName(itemStack: ItemStack, new: Int) {
        val level = itemStack.getLevel()
        val name: String = if (level == 0) {
            config.getString("name", "{name} §6+{level}")!!
                .replace("{name}", Items.getName(itemStack))
                .replace("{level}", level.toString())
                .colored()
        } else {
            Items.getName(itemStack)
        }
        //name = args[0]
        val levels = if (new >= 0) {
            new.toString()
        } else {
            "0"
        }
        val args = name.split(config.getString("fix", " §6+")!!)
        val news = config.getString("name", "{name} §6+{level}")!!
            .replace("{name}", args[0])
            .replace("{level}", levels)
            .colored()
        Items.replaceName(itemStack, Items.getName(itemStack), news)
    }

    fun ItemStack.getLevel(): Int {
        val thi = ChatColor.stripColor(Items.getName(this))?.toLowerCase() ?: "name"
        val name = thi.replace("[^0-9]".toRegex(), "")
        if (name.isEmpty()) {
            return 0
        }
        return name.toIntOrNull() ?: 0
    }

}