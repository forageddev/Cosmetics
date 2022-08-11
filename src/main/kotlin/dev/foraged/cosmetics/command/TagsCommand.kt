package dev.foraged.cosmetics.command

import dev.foraged.commons.acf.annotation.CommandAlias
import dev.foraged.commons.acf.annotation.Description
import dev.foraged.commons.annotations.commands.AutoRegister
import dev.foraged.commons.command.GoodCommand
import dev.foraged.cosmetics.tag.TagService
import net.evilblock.cubed.menu.template.menu.TemplateMenu
import org.bukkit.entity.Player

@AutoRegister
object TagsCommand : GoodCommand()
{
    @CommandAlias("tags|titles|tagsmenu|prefix|suffix|prefixes|tagmenu|prefixmenu|titlemenu|suffixes")
    @Description("Open the tags display menu")
    fun tags(player: Player) {
        TemplateMenu(TagService.template).openMenu(player)
    }
}