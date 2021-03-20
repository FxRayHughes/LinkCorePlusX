package ray.mintcat.linkcoreplus.bind

import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.util.Features
import io.izzel.taboolib.util.item.Items
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import ray.mintcat.linkcoreplus.LinkCorePlus
import ray.mintcat.linkcoreplus.utils.Helper
import ray.mintcat.linkcoreplus.utils.Message

@TListener
class BindListener : Listener, Message {

    @EventHandler
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        val item = event.itemDrop.itemStack
        if (BindItem.hasBind(item)) {
            event.isCancelled = true
            event.player.errors("Bind", "undrop", "该物品被绑定 无法丢弃!")
            return
        }
    }

    @EventHandler
    fun onInventoryClickEvent(event: InventoryClickEvent) {
        val flag = LinkCorePlus.settings.getStringList("bind.binv")
        val item = event.currentItem
        if (Items.isNull(item)) {
            return
        }
        if (!BindItem.hasBind(item)) {
            return
        }
        val inv = event.inventory.name
        flag.forEach {
            if (inv.indexOf(it) != -1) {
                event.isCancelled = true
                event.whoClicked.errors("Bind", "unmove", "该物品被绑定 无法移动!")
            }
        }
    }

    @EventHandler
    fun onPlayerCommandPreprocessEvent(event: PlayerCommandPreprocessEvent) {
        val item = event.player.inventory.itemInMainHand
        if (Items.isNull(item) || !Items.hasLore(item)) {
            return
        }
        if (!BindItem.canBind(item) || !BindItem.hasBind(item)) {
            return
        }
        val list = LinkCorePlus.settings.getStringList("bind.bcmd")
        var boolean = false
        list.forEach {
            if (event.message.startsWith(it, true)) {
                event.isCancelled = true
                boolean = true
            }
        }
        if (boolean) {
            event.player.errors("Bind", "uncmd", "手持该物品无法执行指令!")
        }
    }

    @EventHandler
    fun onEntityPickupItemEvent(event: EntityPickupItemEvent) {
        val player = event.entity as? Player ?: return
        if (!Items.hasLore(event.item.itemStack)) {
            return
        }
        if (BindItem.canBind(event.item.itemStack)) {
            return
        }
        val who = BindItem.whoBind(event.item.itemStack) ?: return
        if (who != player.name) {
            event.isCancelled = true
            player.errors("Bind", "unpick", "这不是属于你的物品!")
        }
    }

    @EventHandler
    fun onPlayerItemHeldEvent(event: PlayerItemHeldEvent) {
        val item = event.player.inventory.getItem(event.newSlot)
        if (!Items.hasLore(item) || BindItem.canBind(item)) {
            return
        }
        val who = BindItem.whoBind(item) ?: return
        if (who != event.player.name) {
            event.isCancelled = true
            Features.dropItem(event.player, item)
            item.amount = 0
            event.player.error("这不是属于你的物品!")
            event.player.errors("Bind", "unheld", "这不是属于你的物品!")
        }
    }

}