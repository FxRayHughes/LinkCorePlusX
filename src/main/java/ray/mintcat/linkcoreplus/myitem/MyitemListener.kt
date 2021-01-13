package ray.mintcat.linkcoreplus.myitem

import io.izzel.taboolib.module.inject.TListener
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

@TListener
class MyitemListener :Listener{

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuit(event:PlayerQuitEvent){
        MyitemFeed.removePlayerData(event.player)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerJoin(event:PlayerJoinEvent){
        MyitemFeed.createPlayerData(event.player)
    }
}