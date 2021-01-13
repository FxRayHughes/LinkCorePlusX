package ray.mintcat.linkcoreplus.invslot

import io.izzel.taboolib.TabooLibAPI
import io.izzel.taboolib.util.Features
import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.item.Items
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import ray.mintcat.linkcoreplus.LinkCorePlus

object InvSlotFeed {


    private val listSlot = mutableListOf<Int>()

    val mapInvSlotIs = mutableMapOf<Player, MutableList<InvSlotIs>>()

    fun loadModel() {
        initListSlots()
        Bukkit.getOnlinePlayers().forEach { loadItems(it) }
    }

    //初始化背包
    fun initializationInv(player: Player) {
        val invSlotIsList = mapInvSlotIs[player] ?: return
        for (invSlotIs in invSlotIsList) {
            val slots = player.inventory.getItem(invSlotIs.slots)
            if (Items.isNull(slots)) {
                player.inventory.setItem(invSlotIs.slots, invSlotIs.itemStack)
            }
        }
    }

    fun loadItems(player: Player) {
        val list = mutableListOf<InvSlotIs>()
        val flag = LinkCorePlus.invslots.getStringColored("Flag", "&010000")
        listSlot.forEach {
            val type = TabooLibAPI.getPluginBridge()
                .setPlaceholders(player, LinkCorePlus.invslots.getStringColored("InvSlot.${it}.item.type", ""))
            val name = TabooLibAPI.getPluginBridge()
                .setPlaceholders(player, LinkCorePlus.invslots.getStringColored("InvSlot.${it}.item.name", ""))
            val lore = TabooLibAPI.getPluginBridge()
                .setPlaceholders(player, LinkCorePlus.invslots.getStringListColored("InvSlot.${it}.item.lore"))
            lore.add(flag)
            val amount = TabooLibAPI.getPluginBridge()
                .setPlaceholders(player, LinkCorePlus.invslots.getStringColored("InvSlot.${it}.item.amount", "1"))
            val damage = TabooLibAPI.getPluginBridge()
                .setPlaceholders(player, LinkCorePlus.invslots.getStringColored("InvSlot.${it}.item.damage", "0"))
            list.add(
                InvSlotIs(toItem(name, lore, type, amount.toInt(),damage.toInt()), it)
            )
        }
        mapInvSlotIs[player] = list
    }

    private fun initListSlots() {
        LinkCorePlus.invslots.getConfigurationSection("InvSlot").getKeys(false).forEach {
            listSlot.add(it.toInt())
        }
    }

    fun runAction(action: String, player: Player) {
        when (action.split(": ")[0]) {
            "cmdP", "command=player" -> Features.dispatchCommand(player, action.split(": ")[1])
            "cmdO", "command=op" -> Features.dispatchCommand(player, action.split(": ")[1], true)
            "cmdS", "command=server" -> Features.dispatchCommand(action.split(": ")[1])
            "tell" -> player.sendMessage(action.split(": ")[1])
            else -> return
        }
    }


    fun toItem(name: String, lore: List<String>, type: String, amount: Int,damage:Int): ItemStack {
        val itemStack = ItemBuilder(Material.matchMaterial(type) ?: Material.AIR)
        itemStack.lore(lore)
        itemStack.name(name)
        itemStack.amount(amount)
        itemStack.damage(damage)
        return itemStack.build()
    }

    fun List<InvSlotIs>.has(itemStack: ItemStack, slot: Int): Boolean {
        this.forEach {
            if (itemStack == it.itemStack && slot == it.slots) {
                return true
            }
        }
        return false
    }


}