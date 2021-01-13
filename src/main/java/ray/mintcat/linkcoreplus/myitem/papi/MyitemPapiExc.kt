package ray.mintcat.linkcoreplus.myitem.papi

import io.izzel.taboolib.module.inject.THook
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import ray.mintcat.linkcoreplus.LinkCorePlus
import ray.mintcat.linkcoreplus.myitem.MyitemFeed
import java.math.RoundingMode
import java.text.DecimalFormat

@THook
class MyitemPapiExc : PlaceholderExpansion() {
    override fun getIdentifier(): String {
        return "linkcoreexc" //myitemplace_stats_damage_max
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
        val maxattr = LinkCorePlus.settings.getDouble("AttributeMaxAbs.${params}", Double.MAX_VALUE)
        val value = MyitemFeed.getExcAttribute(player, params)
        if (value > maxattr){
            return getNoMoreThanTwoDigits(maxattr)
        }
        return if(getNoMoreThanTwoDigits(value) == "-0"){
            "0"
        }else{
            getNoMoreThanTwoDigits(value)
        }
    }

    private fun getNoMoreThanTwoDigits(number: Double): String {
        val format = DecimalFormat("0.##")
        //未保留小数的舍弃规则，RoundingMode.FLOOR表示直接舍弃。
        format.roundingMode = RoundingMode.FLOOR
        return format.format(number)
    }
}