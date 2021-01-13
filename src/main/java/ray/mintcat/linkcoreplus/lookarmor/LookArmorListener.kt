package ray.mintcat.linkcoreplus.lookarmor

import io.izzel.taboolib.module.inject.TListener
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent

@TListener
class LookArmorListener : Listener {

    @EventHandler
    fun onPlayerInteractEntityEvent(event: PlayerInteractEntityEvent) {
        val target = event.rightClicked as? Player ?: return
        val player = event.player ?: return
        if (player.isSneaking){
            LookArmorFeed.playerMap[player] = target
            LookArmorFeed.openMenu(player,target)
        }

    }

}