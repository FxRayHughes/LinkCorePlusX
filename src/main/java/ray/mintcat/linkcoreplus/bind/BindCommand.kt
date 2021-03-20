package ray.mintcat.linkcoreplus.bind

import io.izzel.taboolib.module.command.base.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@BaseCommand(name = "bind", aliases = ["lcb"], permission = "*")
class BindCommand : BaseMainCommand() {

    @SubCommand(permission = "*", description = "绑定手中物品")
    var to: BaseSubCommand = object : BaseSubCommand() {
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = sender as? Player ?: return
            BindItem.toBind(player)
        }
    }

    @SubCommand(permission = "*", description = "解绑手中物品")
    var un: BaseSubCommand = object : BaseSubCommand() {
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = sender as? Player ?: return
            BindItem.unBind(player)
        }
    }

}