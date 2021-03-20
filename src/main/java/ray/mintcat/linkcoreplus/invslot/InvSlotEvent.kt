package ray.mintcat.linkcoreplus.invslot

import com.sucy.skill.api.event.PlayerManaGainEvent
import com.sucy.skill.api.event.PlayerManaLossEvent
import io.izzel.taboolib.TabooLibAPI
import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.util.item.Items
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemHeldEvent
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
            InvSlotFeed.eval(player, LinkCorePlus.invslots.getStringList("InvSlot.${event.slot}.action"))
            return
        }
        val flag = LinkCorePlus.invslots.getStringColored("Flag", "&010000")
        if (Items.hasLore(event.currentItem, flag)) {
            event.currentItem.amount = 0
        }

    }

    @EventHandler
    fun onPlayerItemHeldEvent(event: PlayerItemHeldEvent) {
        val item = event.player.inventory.getItem(event.newSlot)
        val flag = LinkCorePlus.invslots.getStringColored("Flag", "&010000")
        if (Items.hasLore(item, flag)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onMana(event: PlayerManaGainEvent) {
        InvSlotFeed.loadItems(event.playerData.player)
        InvSlotFeed.initializationInv(event.playerData.player)
    }

    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {
        val flag = LinkCorePlus.invslots.getStringColored("Flag", "&010000")
        if (Items.hasLore(event.itemDrop.itemStack, flag)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onMana(event: PlayerManaLossEvent) {
        InvSlotFeed.loadItems(event.playerData.player)
        InvSlotFeed.initializationInv(event.playerData.player)
    }
}