package ray.mintcat.linkcoreplus.invslot

import io.izzel.taboolib.TabooLibAPI
import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.util.item.Items
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import ray.mintcat.linkcoreplus.LinkCorePlus
import ray.mintcat.linkcoreplus.invslot.InvSlotFeed.has

@TListener
class InvSlotEvent : Listener {

    @EventHandler
    fun onPlayerOpenInventoryEvent(event: InventoryOpenEvent) {
        InvSlotFeed.loadItems(event.player as Player)
        InvSlotFeed.initializationInv(event.player as Player)
    }

    @EventHandler
    fun onInventoryInteractEvent(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        InvSlotFeed.loadItems(player)
        val list = InvSlotFeed.mapInvSlotIs[player] ?: return
        InvSlotFeed.initializationInv(player)
        if (list.has(event.currentItem ?: return, event.slot)) {
            event.isCancelled = true
            val actions = TabooLibAPI.getPluginBridge()
                .setPlaceholders(player, LinkCorePlus.invslots.getStringList("InvSlot.${event.slot}.action"))
            actions.forEach {
                InvSlotFeed.runAction(it, player)
            }
            return
        }
        val flag = LinkCorePlus.invslots.getStringColored("Flag", "&010000")
        if (Items.hasLore(event.currentItem,flag)){
            event.currentItem.amount = 0
        }

    }
}