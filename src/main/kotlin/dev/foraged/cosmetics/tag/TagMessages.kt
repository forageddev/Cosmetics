package dev.foraged.cosmetics.tag

import dev.foraged.cosmetics.CosmeticsExtendedPlugin
import gg.scala.aware.annotation.Subscribe
import gg.scala.aware.message.AwareMessage
import gg.scala.store.storage.type.DataStoreStorageType
import java.util.*

object TagMessages
{
    @Subscribe("TagUpdate")
    fun onTagUpdate(message: AwareMessage) {
        val tag = message.retrieve<UUID>("TagIdentifier")
        TagService.tags.removeIf {
            it.identifier == tag
        }

        TagService.tagController.load(tag, DataStoreStorageType.MONGO).thenAccept {
            CosmeticsExtendedPlugin.instance.logger.info("[Tag] Received tag update for $tag")
            if (it != null)  {
                CosmeticsExtendedPlugin.instance.logger.info("[Tag] Result: Created or Updated tag")
                TagService.tags.add(it)
            } else CosmeticsExtendedPlugin.instance.logger.info("[Tag] Result: Deleted tag")
        }
    }
}