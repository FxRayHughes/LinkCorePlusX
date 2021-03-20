package ray.mintcat.linkcoreplus.cooldown

import com.sucy.skill.api.event.PhysicalDamageEvent
import com.sucy.skill.api.event.PlayerCastSkillEvent
import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.util.item.Items
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import ray.mintcat.linkcoreplus.LinkCorePlus
import ray.mintcat.linkcoreplus.invslot.InvSlotFeed

@TListener
class CooldownListener : Listener {

    @EventHandler
    fun onPlayerCastSkillEvent(event: PlayerCastSkillEvent) {
        val skill = event.skill.data.name
        val list = LinkCorePlus.settings.getStringList("cooldown.skills")
        val player = event.player
        if (!list.contains(skill)) {
            return
        }
        if (player.hasCooldown(player.inventory.itemInMainHand.type)) {
            event.isCancelled = true
            return
        }
        eval(player)
    }

    @EventHandler
    fun onPhysicalDamageEvent(event: PhysicalDamageEvent) {
        val player = event.damager as? Player ?: return
        if (player.hasCooldown(player.inventory.itemInMainHand.type)) {
            event.isCancelled = true
            return
        }
        eval(player)
    }

    private fun eval(player: Player) {
        val item = player.inventory.itemInMainHand
        val cd = LinkCorePlus.settings.getString("cooldown.lore", "0.0")
        if (!Items.hasLore(item, cd)) {
            return
        }
        val info = item.itemMeta.lore.firstOrNull { it.indexOf(cd) != -1 } ?: return
        val time = (info.replace("§+[a-z0-9]".toRegex(), "").replace("[^0-9.]".toRegex(), "").toDouble() * 20)
        player.setCooldown(item.type, time.toInt())
        Bukkit.getScheduler().runTaskLaterAsynchronously(LinkCorePlus.plugin, {
            val inflows = LinkCorePlus.settings.getStringListColored("cooldown.action")
            InvSlotFeed.eval(player, inflows)
        }, time.toLong())
    }

}