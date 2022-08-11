package dev.foraged.cosmetics.map.color

import dev.foraged.commons.persist.RegisterMap
import dev.foraged.commons.persist.impl.EnumerablePersistMap
import org.bukkit.ChatColor

@RegisterMap
object ChatColorPersistMap : EnumerablePersistMap<ChatColor>("EquippedColors", "EquippedColor", true)
{
    override var usedGlobally = true

    override fun getKotlinObject(str: String?): ChatColor {
        return str?.let { ChatColor.valueOf(it) } ?: ChatColor.WHITE
    }
}