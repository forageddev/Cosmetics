package dev.foraged.cosmetics.tag

import com.google.common.base.Charsets
import com.google.common.io.Files
import dev.foraged.commons.annotations.Listeners
import dev.foraged.commons.persist.PluginService
import dev.foraged.cosmetics.menu.TagsMenu
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import net.evilblock.cubed.serializers.Serializers
import org.bukkit.event.Listener
import xyz.mkotb.configapi.comment.HeaderComment
import java.io.File
import dev.foraged.cosmetics.CosmeticsExtendedPlugin.Companion.instance;
import gg.scala.aware.message.AwareMessage
import gg.scala.flavor.service.Close
import gg.scala.store.controller.DataStoreObjectController
import gg.scala.store.controller.DataStoreObjectControllerCache
import gg.scala.store.storage.type.DataStoreStorageType

@Service
@HeaderComment("Created kits go here")
@Listeners
object TagService : Listener, PluginService
{
    private val tagsMenuTemplate: File = File(instance.dataFolder, "tags-menu.json")
    val tags = mutableListOf<Tag>()
    val tagController: DataStoreObjectController<Tag> = DataStoreObjectControllerCache.create()
    lateinit var template: TagsMenu

    @Configure
    override fun configure()
    {
        tagsMenuTemplate.parentFile.mkdirs()

        if (tagsMenuTemplate.exists()) {
            Files.newReader(tagsMenuTemplate, Charsets.UTF_8).use { reader ->
                template = Serializers.gson.fromJson(reader, TagsMenu::class.java) as TagsMenu
            }
        } else {
            template = TagsMenu()
        }

        tagController.loadAll(DataStoreStorageType.MONGO).thenAccept {
            it.values.forEach { tag ->
                tags.removeIf { it.identifier == tag.identifier }
                tags.add(tag)
            }
        }
    }

    @Close
    fun close() {
        saveTemplate()

        tags.forEach {
            tagController.save(it, DataStoreStorageType.MONGO)
        }
    }

    fun saveTemplate() {
        try {
            Files.write(Serializers.gson.toJson(template, TagsMenu::class.java), tagsMenuTemplate, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            instance.logger.severe("Failed to save tags-template.json!")
        }
    }

    fun registerTag(tag: Tag) {
        if (findTag(tag.id) != null) {
            tags.removeIf { it.id == tag.id }
            instance.logger.info("[Tag] Updated tag with id ${tag.id}.")
        } else {
            instance.logger.info("[Tag] Registered new tag with id ${tag.id}.")
        }
        tags.add(tag)
        tag.saveUpdates()
    }

    fun unregisterTag(tag: Tag) {
        tags.removeIf { it.identifier == tag.identifier }
        tagController.delete(tag.identifier, DataStoreStorageType.MONGO).thenAccept {
            AwareMessage.of(
                "TagUpdate", instance.aware,
                "TagIdentifier" to tag.identifier
            ).publish()
        }
    }

    fun findTag(name: String) : Tag? {
        return tags.find { it.id == name }
    }
}