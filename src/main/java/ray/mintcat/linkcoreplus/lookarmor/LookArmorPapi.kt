package ray.mintcat.linkcoreplus.lookarmor

import io.izzel.taboolib.module.inject.THook
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player


@THook
class LookArmorPapi : PlaceholderExpansion() {

    override fun getIdentifier(): String {
        return "looker"
    }

    override fun getAuthor(): String {
        return "枫溪"
    }

    override fun getVersion(): String {
        return "1.0.1"
    }

    override fun persist(): Boolean {
        return true
    }

    override fun onPlaceholderRequest(player: Player, params: String): String {
        return when (params){
            "target" -> LookArmorFeed.playerMap[player]?.name ?: "Null"
            else -> "Null"
        }
    }
}
