package dev.foraged.cosmetics.map.tag

import dev.foraged.commons.persist.PersistMap
import dev.foraged.cosmetics.tag.Tag
import dev.foraged.cosmetics.tag.TagService

object TagPersistMap : PersistMap<Tag>("EquippedTags", "EquippedTag", true)
{
    override var usedGlobally = true

    override fun getKotlinObject(str: String?): Tag {
        return TagService.findTag(str ?: "null") ?: throw NullPointerException("Tag not found.")
    }

    override fun getMongoValue(t: Tag): Any {
        return t.id
    }

    override fun getRedisValue(t: Tag): String {
        return t.id
    }
}