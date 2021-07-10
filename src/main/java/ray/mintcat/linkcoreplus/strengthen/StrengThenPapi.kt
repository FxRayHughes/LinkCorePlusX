package ray.mintcat.linkcoreplus.strengthen

import io.izzel.taboolib.module.compat.PlaceholderHook
import io.izzel.taboolib.module.inject.THook
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import ray.mintcat.linkcoreplus.LinkCorePlus
import ray.mintcat.linkcoreplus.strengthen.StrengThenFeed.getLevel


@THook
class StrengThenPapi : PlaceholderHook.Expansion {

    override fun plugin(): Plugin {
        return LinkCorePlus.plugin
    }

    override fun identifier(): String {
        return "streng"
    }


    override fun onPlaceholderRequest(player: Player, params: String): String {
        return when (params) {
            "强化等级" -> {
                return player.inventory.itemInMainHand.getLevel().toString()
            }
            "成功率" -> {
                return StrengThenFeed.upWeight(player, player.inventory.itemInMainHand.getLevel() + 1).toString() + "%"
            }
            "材料需求" -> {
                return StrengThenFeed.config.getConfigurationSection("levels.${player.inventory.itemInMainHand.getLevel()}").getStringList("items").joinToString("/n")
            }
            else -> "Null"
        }
    }
}
