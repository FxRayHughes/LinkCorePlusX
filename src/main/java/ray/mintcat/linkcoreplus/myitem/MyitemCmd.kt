package ray.mintcat.linkcoreplus.myitem

import io.izzel.taboolib.module.command.base.*
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.permissions.PermissionDefault
import ray.mintcat.linkcoreplus.LinkCorePlus
import java.math.RoundingMode
import java.text.DecimalFormat

@BaseCommand(name = "linkcoreplus", aliases = ["link", "linkcore"], permission = "linkpro.admin")
class MyitemCmd : BaseMainCommand() {

    @SubCommand
    var edit: BaseSubCommand = object : BaseSubCommand() {
        override fun getDescription(): String {
            return "设置临时属性"
        }

        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("目标"), Argument("属性"), Argument("公式"), Argument("持续时间[tick]"))
        }

        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Bukkit.getPlayerExact(args[0])
            if (player == null) {
                sender.sendMessage("§f§l[§fLinkCore§f]§l " + "§7目标 §f" + args[0] + " §7离线.")
                return
            }
            MyitemFeed.editAttribute(player, args[1], args[2], args[3].toLong())
        }
    }

    @SubCommand
    var editabs: BaseSubCommand = object : BaseSubCommand() {
        override fun getDescription(): String {
            return "设置绝对临时属性"
        }

        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("目标"), Argument("属性"), Argument("数值"), Argument("持续时间[tick]"))
        }

        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Bukkit.getPlayerExact(args[0])
            if (player == null) {
                sender.sendMessage("§f§l[§fLinkCore§f]§l " + "§7目标 §f" + args[0] + " §7离线.")
                return
            }
            MyitemFeed.editAbsAttribute(player, args[1], args[2], args[3].toLong())
        }
    }

    @SubCommand
    var editexc: BaseSubCommand = object : BaseSubCommand() {
        override fun getDescription(): String {
            return "设置额外临时属性"
        }

        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("目标"), Argument("属性"), Argument("数值"), Argument("持续时间[tick]"))
        }

        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Bukkit.getPlayerExact(args[0])
            if (player == null) {
                sender.sendMessage("§f§l[§fLinkCore§f]§l " + "§7目标 §f" + args[0] + " §7离线.")
                return
            }
            MyitemFeed.editExcAttribute(player, args[1], args[2], args[3].toLong())
        }
    }

    @SubCommand
    var look: BaseSubCommand = object : BaseSubCommand() {
        override fun getDescription(): String {
            return "查询属性信息"
        }

        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("目标"), Argument("属性") { listOf<String>("啊这") })
        }

        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Bukkit.getPlayerExact(args[0])
            if (player == null) {
                sender.sendMessage("§f§l[§fLinkCore§f]§l " + "§7目标 §f" + args[0] + " §7离线.")
                return
            }
            player.sendMessage("""
                |-
                §7属性:§f ${args[1]}
                §7玩家:§f ${args[0]}
                §7最大值:§f ${LinkCorePlus.settings.getDouble("AttributeMaxAbs.${args[1]}", Double.MAX_VALUE)}
                §7返回值:§f ${MyitemFeed.getAttribueInfo(player, args[1])}
                §7玩家临时参数:§f ${MyitemFeed.getAttribute(player, args[1])}
                §7玩家绝对参数:§f ${MyitemFeed.getAbsAttribute(player, args[1])}
                §7玩家额外参数:§f ${MyitemFeed.getExcAttribute(player, args[1])}
                §7装备参数:§f ${MyitemAttribute.getAttribute(player, args[1])}
                |-
            """.trimIndent())
        }
    }
}