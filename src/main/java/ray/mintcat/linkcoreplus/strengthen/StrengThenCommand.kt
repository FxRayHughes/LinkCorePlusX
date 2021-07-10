package ray.mintcat.linkcoreplus.strengthen

import io.izzel.taboolib.module.command.base.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ray.mintcat.linkcoreplus.strengthen.StrengThenFeed.setLevelLore
import ray.mintcat.linkcoreplus.strengthen.StrengThenFeed.setLevelName

@BaseCommand(name = "streng", aliases = ["streng"], permission = "*")
class StrengThenCommand : BaseMainCommand() {

    @SubCommand(permission = "*", description = "打开UI")
    var open: BaseSubCommand = object : BaseSubCommand() {
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = sender as? Player ?: return
            StrengThen.openGUI(player)
        }
    }

    @SubCommand(permission = "*", description = "设置等级")
    var set: BaseSubCommand = object : BaseSubCommand() {

        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("等级"))
        }


        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = sender as? Player ?: return
            val item = player.inventory.itemInMainHand
            val add = args[0].toIntOrNull() ?: 0
            setLevelName(item, add)
            setLevelLore(item, add)
        }
    }

}