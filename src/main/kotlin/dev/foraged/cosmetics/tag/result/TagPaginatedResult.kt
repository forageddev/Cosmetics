package dev.foraged.cosmetics.tag.result

import dev.foraged.cosmetics.tag.Tag
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Constants
import net.evilblock.cubed.util.bukkit.PaginatedResult

object TagPaginatedResult : PaginatedResult<Tag>()
{
    override fun getHeader(page: Int, maxPages: Int) = "${CC.PRI}=== ${CC.SEC}Tags ${CC.WHITE}($page/$maxPages) ${CC.PRI}==="

    override fun format(result: Tag, resultIndex: Int): String
    {
        return " ${CC.GRAY}${Constants.DOUBLE_ARROW_RIGHT} ${result.displayName} ${CC.GRAY}(Enabled: ${result.enabled})"
    }
}