package dev.foraged.cosmetics.tag

import dev.foraged.cosmetics.CosmeticsExtendedPlugin
import dev.foraged.cosmetics.map.tag.TagPersistMap
import gg.scala.aware.message.AwareMessage
import gg.scala.store.storage.storable.IDataStoreObject
import gg.scala.store.storage.type.DataStoreStorageType
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.message.FancyMessage
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

class Tag(
    var id: String,
    var name: String,
    var displayName: String,
    var chatComponent: FancyMessage.SerializableComponent = FancyMessage.SerializableComponent(name),
    var permission: String = "tags.use.${id}",
    var order: Int = 1,
    var icon: ItemStack = ItemBuilder.of(Material.NAME_TAG).build(),
    var enabled: Boolean = false,
    override val identifier: UUID = UUID.randomUUID()
) : IDataStoreObject {
    companion object {
        val CHAT_PREFIX = "${CC.B_PRI}[Tags] "
    }

    fun apply(target: Player) {
        TagPersistMap[target.uniqueId] = this
    }

    fun saveUpdates() {
        TagService.tagController.save(this, DataStoreStorageType.MONGO).thenAccept {
            AwareMessage.of(
                "TagUpdate", CosmeticsExtendedPlugin.instance.aware,
                "TagIdentifier" to identifier
            ).publish()
        }
    }
}