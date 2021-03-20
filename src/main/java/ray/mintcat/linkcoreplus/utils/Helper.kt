package ray.mintcat.linkcoreplus.utils

import io.izzel.taboolib.kotlin.kether.KetherShell
import io.izzel.taboolib.kotlin.kether.common.util.LocalizedException
import io.izzel.taboolib.module.inject.TInject
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.lite.cooldown.Cooldown
import org.bukkit.Effect
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ray.mintcat.linkcoreplus.LinkCorePlus

interface Helper {

    fun CommandSender.info(value: String) {
        this.sendMessage("${Global.system}${value.replace("&", "§")}")
        if (this is Player && !Global.cd.isCooldown(this.name)) {
            this.playSound(this.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 2f)
        }
    }

    fun CommandSender.error(value: String) {
        this.sendMessage("§c${Global.system}§7${value.replace("&", "§")}")
        if (this is Player && !Global.cd.isCooldown(this.name)) {
            this.playSound(this.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
        }
    }

    fun Player.info(value: String) {
        this.sendMessage("${Global.system}${value.replace("&", "§")}")
        if (!Global.cd.isCooldown(this.name)) {
            this.playSound(this.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 2f)
        }
    }

    fun Player.error(value: String) {
        this.sendMessage("§c${Global.system}§7${value.replace("&", "§")}")
        if (!Global.cd.isCooldown(this.name)) {
            this.playSound(this.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
        }
    }

    fun Block.display() {
        world.playEffect(location, Effect.STEP_SOUND, type)
    }

    fun String.unColored(): String {
        return TLocale.Translate.setUncolored(this)
    }

    object Global {

        @TInject
        val cd = Cooldown("command.sound", 50)
        @TInject
        val system = LinkCorePlus.settings.getStringColored("pluginName")
    }

    fun eval(player: Player, action: List<String>) {
        try {
            KetherShell.eval(action) {
                sender = player
            }
        } catch (e: LocalizedException) {
            e.print()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun LocalizedException.print() {
        println("[Ketherx] Unexpected exception while parsing kether shell:")
        localizedMessage.split("\n").forEach {
            println("[Ketherx] $it")
        }
    }
}