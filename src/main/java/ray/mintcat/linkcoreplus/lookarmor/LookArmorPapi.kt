package ray.mintcat.linkcoreplus.lookarmor

import io.izzel.taboolib.module.compat.PlaceholderHook
import io.izzel.taboolib.module.inject.THook
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import ray.mintcat.linkcoreplus.LinkCorePlus


@THook
class LookArmorPapi : PlaceholderHook.Expansion {

    override fun plugin(): Plugin {
        return LinkCorePlus.plugin
    }

    override fun identifier(): String {
        return "looker"
    }


    override fun onPlaceholderRequest(player: Player, params: String): String {
        return when (params){
            "target" -> LookArmorFeed.playerMap[player]?.name ?: "Null"
            else -> "Null"
        }
    }
}
