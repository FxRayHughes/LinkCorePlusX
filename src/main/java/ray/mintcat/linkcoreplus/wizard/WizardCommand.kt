package ray.mintcat.linkcoreplus.wizard

import ink.ptms.cronus.CronusAPI
import ink.ptms.cronus.database.data.DataPlayer
import ink.ptms.cronus.internal.program.NoneProgram
import ink.ptms.cronus.internal.variable.VariableExecutor
import ink.ptms.cronus.internal.variable.impl.EngineG
import ink.ptms.cronus.internal.variable.impl.EngineY
import ink.ptms.cronus.service.guide.GuideWay
import ink.ptms.cronus.uranus.function.FunctionParser
import io.izzel.taboolib.module.command.base.*
import io.izzel.taboolib.util.ArrayUtil
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault
import ray.mintcat.linkcoreplus.Helper
import ray.mintcat.linkcoreplus.LinkCorePlus
import ray.mintcat.linkcoreplus.wizard.WizardFeed.toWizardType
@BaseCommand(name = "wizard",aliases = ["wizvar"],permissionDefault = PermissionDefault.OP)
class WizardCommand:BaseMainCommand(),Helper {

    override val system: String
        get() = LinkCorePlus.settings.getStringColored("pluginName")

    @SubCommand
    var info: BaseSubCommand = object : BaseSubCommand() {
        override fun getDescription(): String {
            return "查询变量"
        }
        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("目标"),Argument("类型") {WizardFeed.typeList}, Argument("信息"))
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Bukkit.getPlayer(args[0]) as? Player ?: return
            val type = args[1].toWizardType() ?: return
            sender.info("${WizardFeed.getInfo(player,type,args[2],"不存在")}")
        }
    }

    @SubCommand
    var server: BaseSubCommand = object : BaseSubCommand() {
        override fun getDescription(): String {
            return "设置全局变量"
        }
        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("键"),Argument("符号") { listOf("+", "-", "=")}, Argument("值"))
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            VariableExecutor.update(EngineG(), args[0], args[1], args[2])
        }
    }

    @SubCommand
    var pvar: BaseSubCommand = object : BaseSubCommand() {
        override fun getDescription(): String {
            return "设置临时变量"
        }
        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("目标"),Argument("键"),Argument("符号") { listOf("+", "-", "=")}, Argument("值"))
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Bukkit.getPlayer(args[0]) ?: return
            val dataPlayer = CronusAPI.getData(player)
            VariableExecutor.update(
                EngineY(dataPlayer.dataTemp, player),
                args[1],
                args[2], FunctionParser.parseAll(NoneProgram(player), args[3])
            )
            dataPlayer.push()
        }
    }

    @SubCommand
    var pval: BaseSubCommand = object : BaseSubCommand() {
        override fun getDescription(): String {
            return "设置永久变量"
        }
        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("键"),Argument("符号") { listOf("+", "-", "=")}, Argument("值"))
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Bukkit.getPlayer(args[0]) ?: return
            val dataPlayer = CronusAPI.getData(player)
            VariableExecutor.update(
                EngineY(dataPlayer.dataGlobal, player),
                args[1],
                args[2], FunctionParser.parseAll(NoneProgram(player), args[3])
            )
            dataPlayer.push()
        }
    }

}