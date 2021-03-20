package ray.mintcat.linkcoreplus.bind

import io.izzel.taboolib.module.compat.PlaceholderHook
import io.izzel.taboolib.module.inject.THook
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import ray.mintcat.linkcoreplus.LinkCorePlus
import ray.mintcat.linkcoreplus.lookarmor.LookArmorFeed

@THook
class BindPapi : PlaceholderHook.Expansion {

    override fun plugin(): Plugin {
        return LinkCorePlus.plugin
    }

    override fun identifier(): String {
        return "bind"
    }


    override fun onPlaceholderRequest(player: Player, params: String): String {
        return when (params) {
            "canbind" -> BindItem.canBind(player.inventory.itemInMainHand).toString()
            "whobind" -> BindItem.whoBind(player.inventory.itemInMainHand) ?: "false"
            "isbind" -> BindItem.isBind(player.inventory.itemInMainHand).toString()
            else -> "false"
        }
    }
}
