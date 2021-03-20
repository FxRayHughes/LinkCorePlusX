package ray.mintcat.linkcoreplus.expup

import com.sucy.skill.SkillAPI
import com.sucy.skill.api.enums.ExpSource
import io.izzel.taboolib.TabooLibAPI
import io.izzel.taboolib.module.command.base.*
import io.izzel.taboolib.util.Features
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ray.mintcat.linkcoreplus.utils.Helper
import ray.mintcat.linkcoreplus.LinkCorePlus
import java.math.RoundingMode
import java.text.DecimalFormat

@BaseCommand(name = "LinkExp",permission = "*")
class ExpUpCommand:BaseMainCommand(), Helper {

    @SubCommand(permission = "*")
    var give: BaseSubCommand = object : BaseSubCommand() {
        override fun getDescription(): String {
            return "给玩家经验"
        }

        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("目标"), Argument("公式"), Argument("加成"))
        }

        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Bukkit.getPlayerExact(args[0]) ?: run {
                sender.sendMessage("§f§l[§fLinkCore§f]§l " + "§7目标 §f" + args[0] + " §7离线.")
                return
            }
            val value = getValues(player,args[1])
            val data = (Features.compileScript(value)?.eval() ?: "0.00").toString().toDouble().no2().toDouble()
            SkillAPI.getPlayerData(player).giveExp(data,ExpSource.COMMAND)
            val message = (Features.compileScript(getValues(player,args[2]))?.eval() ?: "0.00").toString().toDouble().no2()
            player.info(LinkCorePlus.attribute.getStringColored("UPEXP.message").replace("{value}",message).replace("{exp}",data.toString()))
        }
        private fun Double.no2(): String {
            val format = DecimalFormat("0.##")
            //未保留小数的舍弃规则，RoundingMode.FLOOR表示直接舍弃。
            format.roundingMode = RoundingMode.FLOOR
            return format.format(this)
        }
        fun getValues(player: Player, value: String): String {
            return TabooLibAPI.getPluginBridge().setPlaceholders(player, value
                .replace("__", ""))
        }
    }
}