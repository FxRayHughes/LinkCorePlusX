package ray.mintcat.linkcoreplus.wizard

import io.izzel.taboolib.TabooLibAPI
import io.izzel.taboolib.module.inject.THook
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import ray.mintcat.linkcoreplus.wizard.WizardFeed.toWizardType

@THook
class WizardPAPI : PlaceholderExpansion() {
    override fun getIdentifier(): String {
        return "wizard"
    }

    override fun getAuthor(): String {
        return "Ray_Hughes"
    }

    override fun getVersion(): String {
        return "1.0"
    }

    override fun persist(): Boolean {
        return true
    }

    override fun onPlaceholderRequest(player: Player, params: String): String {
        val param = TabooLibAPI.getPluginBridge().setPlaceholders(player,params.replace("{","%").replace("}","%")).split("_")
        return when (param[0]) {
            "info" -> {
                //%konintegral_info_type_key_def% ?
                WizardFeed.getInfo(player,param[1].toWizardType() ?:return "PlayerVal",param[2],param[3]).toString()
            }
            "has" -> {

                //%wizard_has_type_key_value_yes_no% ?
                //             0   1   2     3   4
                val value = WizardFeed.getInfo(player,param[0].toWizardType() ?:return "PlayerVal",param[1],0).toString().toInt()
                if (value >= param[2].toInt()) {
                    param[3]
                } else {
                    param[4]
                }
            }
            "is" -> {
                //%wizard_is_key_value_yes_no% ?
                //              0   1   2     3   4
                val value = WizardFeed.getInfo(player,param[0].toWizardType() ?:return "PlayerVal",param[1]," ").toString()
                if (value.equals(param[2],true)) {
                    param[3]
                } else {
                    param[4]
                }
            }
            else -> {
                "N/A"
            }
        }
    }
}