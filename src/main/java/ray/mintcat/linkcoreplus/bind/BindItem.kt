package ray.mintcat.linkcoreplus.bind

import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.item.Items
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import ray.mintcat.linkcoreplus.utils.Helper
import ray.mintcat.linkcoreplus.LinkCorePlus

object BindItem : Helper {

    fun isBind(itemStack: ItemStack): Boolean {
        val flag = LinkCorePlus.settings.getStringColored("bind.to").replace("{player}", "")
        return Items.hasLore(itemStack, flag)
    }

    fun canBind(itemStack: ItemStack): Boolean {
        val flag = LinkCorePlus.settings.getStringColored("bind.can")
        return Items.hasLore(itemStack, flag)
    }

    fun whoBind(itemStack: ItemStack): String? {
        if (!Items.hasLore(itemStack)) {
            return null
        }
        val flag = LinkCorePlus.settings.getStringColored("bind.to").replace("{player}", "")
        return itemStack.itemMeta.lore.firstOrNull() { it.indexOf(flag) != -1 }?.replace(flag, "")
    }

    fun hasBind(itemStack: ItemStack): Boolean {
        if (whoBind(itemStack) == null) {
            return false
        }
        return true
    }

    fun toBind(player: Player) {
        if (Items.isNull(player.inventory.itemInMainHand)) {
            return
        }
        val new = LinkCorePlus.settings.getStringColored("bind.to").replace("{player}", player.name)
        val item = replace(player.inventory.itemInMainHand, LinkCorePlus.settings.getStringColored("bind.can"), new)
        player.inventory.itemInMainHand = item
    }

    fun unBind(player: Player) {
        if (Items.isNull(player.inventory.itemInMainHand) || !Items.hasLore(player.inventory.itemInMainHand)) {
            return
        }
        val flag = LinkCorePlus.settings.getStringColored("bind.to").replace("{player}", "")
        val info = player.inventory.itemInMainHand.itemMeta.lore.firstOrNull { it.indexOf(flag) != -1 } ?: return
        Items.replaceLore(player.inventory.itemInMainHand, info, LinkCorePlus.settings.getStringColored("bind.can"))
    }

    fun replace(itemStack: ItemStack, oldLore: String?, newLore: String): ItemStack {
        val lore = itemStack.itemMeta.lore.toMutableList()
        val build = mutableListOf<String>()
        if (!canBind(itemStack)) {
            return itemStack
        }
        if (oldLore == null) {
            build.addAll(lore)
            build.add(newLore)
            return ItemBuilder(itemStack).lore(build).build()
        }
        lore.forEach {
            if (it.indexOf(oldLore) != -1) {
                build.add(newLore)
            } else {
                build.add(it)
            }

        }
        return ItemBuilder(itemStack).lore(build).build()
    }

}