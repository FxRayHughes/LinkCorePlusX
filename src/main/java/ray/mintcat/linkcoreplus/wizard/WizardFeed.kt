package ray.mintcat.linkcoreplus.wizard

import ink.ptms.cronus.Cronus
import ink.ptms.cronus.CronusAPI

import org.bukkit.entity.Player

object WizardFeed {

    fun getInfo(player: Player, type: WizardType, info: Any, def: Any): Any {
        val dataPlayer = CronusAPI.getData(player)
        return when (type) {
            WizardType.SERVER -> Cronus.getCronusService().database.getGlobalVariable(info.toString()) ?: def
            WizardType.PLAYERVAL -> dataPlayer.dataGlobal.get(info.toString(), def)
            WizardType.PLAYERVAR -> dataPlayer.dataTemp.get(info.toString(), def)
        }
    }

    fun String.toWizardType():WizardType?{
        return when (this){
            "Server","server","s","ser" -> WizardType.SERVER
            "PlayerVal","playerval","pv","val" ->WizardType.PLAYERVAL
            "PlayerVar","playervar","pr","var" -> WizardType.PLAYERVAR
            else -> null
        }
    }

    val typeList = mutableListOf("Server","PlayerVal","PlayerVar")

}