package ray.mintcat.linkcoreplus.utils

import io.izzel.taboolib.TabooLibAPI
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ray.mintcat.linkcoreplus.LinkCorePlus

interface Message : Helper {

    fun Player.title(group: String, info: String, def: String) {
        val key = LinkCorePlus.message.getStringColored("${group}.${info}", def)
        this.info(key.papi(this))
    }

    fun Player.errors(group: String, info: String, def: String) {
        val key = LinkCorePlus.message.getStringColored("${group}.${info}", def)
        this.error(key.papi(this))
    }

    fun CommandSender.title(group: String, info: String, def: String) {
        val key = LinkCorePlus.message.getStringColored("${group}.${info}", def)
        this.info(key)
    }

    fun CommandSender.errors(group: String, info: String, def: String) {
        val key = LinkCorePlus.message.getStringColored("${group}.${info}", def)
        this.error(key)
    }

    fun String.papi(player: Player): String {
        return TabooLibAPI.getPluginBridge().setPlaceholders(player, this)
    }

}