package dev.foraged.cosmetics.color

import com.google.common.base.Charsets
import com.google.common.io.Files
import dev.foraged.commons.persist.PluginService
import dev.foraged.cosmetics.CosmeticsExtendedPlugin
import dev.foraged.cosmetics.menu.ChatColorMenu
import dev.foraged.cosmetics.menu.TagsMenu
import dev.foraged.cosmetics.tag.TagService
import gg.scala.flavor.service.Close
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import net.evilblock.cubed.serializers.Serializers
import java.io.File

@Service
object ChatColorService : PluginService
{
    private val colorMenuTemplate: File = File(CosmeticsExtendedPlugin.instance.dataFolder, "color-menu.json")
    lateinit var template: ChatColorMenu

    @Configure
    override fun configure()
    {
        colorMenuTemplate.parentFile.mkdirs()

        if (colorMenuTemplate.exists()) {
            Files.newReader(colorMenuTemplate, Charsets.UTF_8).use { reader ->
                template = Serializers.gson.fromJson(reader, ChatColorMenu::class.java) as ChatColorMenu
            }
        } else {
            template = ChatColorMenu()
        }
    }

    @Close
    fun close() {
        try {
            Files.write(Serializers.gson.toJson(template, ChatColorMenu::class.java),
                colorMenuTemplate, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            CosmeticsExtendedPlugin.instance.logger.severe("Failed to save color-menu.json!")
        }
    }
}