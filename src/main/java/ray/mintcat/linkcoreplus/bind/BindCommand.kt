package ray.mintcat.linkcoreplus.bind

import io.izzel.taboolib.module.command.base.*
import io.izzel.taboolib.module.locale.chatcolor.TColor
import io.izzel.taboolib.util.item.Items
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ray.mintcat.linkcoreplus.LinkCorePlus
import ray.mintcat.linkcoreplus.utils.Helper

@BaseCommand(name = "bind", aliases = ["lcb"], permission = "*")
class BindCommand : BaseMainCommand(), Helper {

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

    @SubCommand(permission = "*", description = "设置外观")
    var look: BaseSubCommand = object : BaseSubCommand() {
        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("目标lore"))
        }

        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = sender as? Player ?: return
            val new = TColor.translate(args[0])
            val key = LinkCorePlus.settings.getStringColored("looks.key")
            val format = LinkCorePlus.settings.getStringColored("looks.format")
            val item = player.inventory.itemInMainHand
            if (!Items.hasLore(item)) {
                return
            }
            val thatsa = item.itemMeta.lore!!.firstOrNull { it.contains(key) } ?: return
            // 外观: 木剑 / 外观: 木剑 (木剑)
            if (thatsa.contains("(")) {
                val keys = thatsa.replace(key, "")
                val oldName = keys.split(" ")[1].replace("(","").replace(")","")
                val newLore = format.replace("{key}", key).replace("{name}", new).replace("{oldname}", oldName)
                Items.replaceLore(item, thatsa, newLore)
                return
            }
            val keys = thatsa.replace(key, "")
            val oldName = keys.split(" ")[0].replace("(","").replace(")","")
            val newLore = format.replace("{key}", key).replace("{name}", new).replace("{oldname}", oldName)
            Items.replaceLore(item, thatsa, newLore)
        }
    }

    @SubCommand(permission = "*", description = "还原外观")
    var unlook: BaseSubCommand = object : BaseSubCommand() {
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = sender as? Player ?: return
            val key = LinkCorePlus.settings.getStringColored("looks.key")
            val format = LinkCorePlus.settings.getStringColored("looks.formatold")
            val item = player.inventory.itemInMainHand
            if (!Items.hasLore(item)) {
                return
            }
            val thatsa = item.itemMeta.lore!!.firstOrNull { it.contains(key) } ?: return
            // 外观: 木剑 / 外观: 木剑 (木剑)
            if (!thatsa.contains("(")) {
                player.error("该物品没有外观!")
                return
            }
            val keys = thatsa.replace(key, "")
            val oldName = keys.split(" ")[1].replace("(","").replace(")","")
            val newLore = format.replace("{key}", key).replace("{oldname}", oldName)
            Items.replaceLore(item, thatsa, newLore)
        }
    }

}