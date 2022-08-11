package dev.foraged.cosmetics.chat

import com.minexd.core.bukkit.chat.impl.GlobalChatChannelComposite
import dev.foraged.cosmetics.CosmeticsExtendedPlugin
import dev.foraged.cosmetics.configuration.CosmeticConfiguration
import dev.foraged.cosmetics.map.tag.TagPersistMap
import net.evilblock.cubed.message.FancyMessage
import org.bukkit.event.player.AsyncPlayerChatEvent

class TagPart : GlobalChatChannelComposite.ChatChannelPart("tag", CosmeticsExtendedPlugin.instance.config<CosmeticConfiguration>().tagOrder)
{
    override fun format(event: AsyncPlayerChatEvent): FancyMessage.SerializableComponent
    {
        val tag = TagPersistMap[event.player.uniqueId] ?: return FancyMessage.SerializableComponent("")

        val component = tag.chatComponent
        component.value = component.value + " "
        return component
    }
}