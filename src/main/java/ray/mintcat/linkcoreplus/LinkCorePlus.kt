package ray.mintcat.linkcoreplus

import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.inject.TInject
import org.serverct.ersha.manager.read.sub.AttributeValueRead
import ray.mintcat.linkcoreplus.invslot.InvSlotFeed

object LinkCorePlus: Plugin() {

    @TInject(value = ["settings.yml"], locale = "LOCALE-PRIORITY")
    lateinit var settings: TConfig
        private set

    @TInject(value = ["invslots.yml"], locale = "LOCALE-PRIORITY")
    lateinit var invslots: TConfig
        private set

    @TInject(value = ["lookplayer.yml"], locale = "LOCALE-PRIORITY")
    lateinit var lookplayer: TConfig
        private set

    @TInject(value = ["attribute.yml"], locale = "LOCALE-PRIORITY")
    lateinit var attribute: TConfig
        private set

    @TInject(value = ["message.yml"], locale = "LOCALE-PRIORITY")
    lateinit var message: TConfig
        private set

    @TInject(value = ["streng.yml"], locale = "LOCALE-PRIORITY")
    lateinit var streng: TConfig
        private set

    override fun onEnable() {
        InvSlotFeed.loadModel()
        AttributeValueRead().unregister()
    }
}