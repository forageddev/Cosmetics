package dev.foraged.cosmetics.chat

import com.minexd.core.bukkit.chat.impl.GlobalChatChannelComposite
import dev.foraged.cosmetics.CosmeticsExtendedPlugin
import dev.foraged.cosmetics.configuration.CosmeticConfiguration
import dev.foraged.cosmetics.map.color.ChatColorPersistMap
import dev.foraged.cosmetics.map.tag.TagPersistMap
import net.evilblock.cubed.message.FancyMessage
import org.bukkit.ChatColor
import org.bukkit.event.player.AsyncPlayerChatEvent

class ColorPart : GlobalChatChannelComposite.ChatChannelPart("color", 125)
{
    override fun format(event: AsyncPlayerChatEvent): FancyMessage.SerializableComponent
    {
        val color = ChatColorPersistMap[event.player.uniqueId] ?: ChatColor.WHITE.toString()
        return FancyMessage.SerializableComponent(color.toString())
    }
}