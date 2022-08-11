package dev.foraged.cosmetics

import com.minexd.core.bukkit.chat.impl.GlobalChatChannelComposite
import dev.foraged.commons.ExtendedPaperPlugin
import dev.foraged.commons.annotations.container.ContainerDisable
import dev.foraged.commons.annotations.container.ContainerEnable
import dev.foraged.commons.config.annotations.ContainerConfig
import dev.foraged.cosmetics.chat.ColorPart
import dev.foraged.cosmetics.configuration.CosmeticConfiguration
import dev.foraged.cosmetics.tag.TagMessages
import dev.foraged.cosmetics.tag.TagService
import dev.foraged.cosmetics.chat.TagPart
import dev.foraged.cosmetics.color.ChatColorService
import gg.scala.aware.Aware
import gg.scala.aware.AwareBuilder
import gg.scala.aware.codec.codecs.interpretation.AwareMessageCodec
import gg.scala.aware.message.AwareMessage
import me.lucko.helper.plugin.ap.Plugin
import me.lucko.helper.plugin.ap.PluginDependency

@Plugin(
    name = "Cosmetics",
    version = "\${git.commit.id.abbrev}",
    depends = [
        PluginDependency("Commons"),
        PluginDependency("Core")
    ]
)
@ContainerConfig(
    value = "config",
    model = CosmeticConfiguration::class,
    crossSync = false
)
class CosmeticsExtendedPlugin : ExtendedPaperPlugin()
{
    companion object {
        lateinit var instance: CosmeticsExtendedPlugin
    }

    lateinit var aware: Aware<AwareMessage>

    @ContainerEnable
    fun containerEnable()
    {
        instance = this

        aware = AwareBuilder
            .of<AwareMessage>("cosmetics")
            .codec(AwareMessageCodec)
            .logger(logger)
            .build()
        aware.listen(TagMessages)
        aware.connect()

        GlobalChatChannelComposite.registerPart(TagPart())
        GlobalChatChannelComposite.registerPart(ColorPart())
    }

    @ContainerDisable
    fun containerDisable() {
        TagService.close()
        ChatColorService.close()
    }
}
