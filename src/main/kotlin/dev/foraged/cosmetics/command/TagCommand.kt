package dev.foraged.cosmetics.command

import dev.foraged.commons.acf.CommandHelp
import dev.foraged.commons.acf.ConditionFailedException
import dev.foraged.commons.acf.annotation.*
import dev.foraged.commons.annotations.commands.AutoRegister
import dev.foraged.commons.annotations.commands.customizer.CommandManagerCustomizer
import dev.foraged.commons.command.CommandManager
import dev.foraged.commons.command.GoodCommand
import dev.foraged.cosmetics.tag.Tag
import dev.foraged.cosmetics.tag.TagService
import dev.foraged.cosmetics.menu.TagsMenu
import dev.foraged.cosmetics.tag.result.TagPaginatedResult
import net.evilblock.cubed.menu.template.menu.EditTemplateLayoutMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.text.TextUtil
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ClickEvent.Action
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("tag")
@CommandPermission("tags.tag.management")
@AutoRegister
object TagCommand : GoodCommand()
{
    @CommandManagerCustomizer
    fun customizer(manager: CommandManager) {
        manager.commandContexts.registerContext(Tag::class.java) {
            val tag = it.popFirstArg().lowercase()
            return@registerContext TagService.findTag(tag) ?: throw ConditionFailedException("There is no tag registered with the id \"${tag}\".")
        }
    }

    @HelpCommand
    fun help(commandHelp: CommandHelp) {
        commandHelp.showHelp()
    }

    @Subcommand("create")
    @Description("Create a new tag")
    fun create(player: Player, name: String, display: String) {
        val tag = Tag(name.lowercase(), name, CC.translate(display),
            enabled = false
        )

        TagService.registerTag(tag)
        player.sendMessage("${CC.B_GREEN}Registered new tag with name $name.")
    }

    @Subcommand("delete")
    @Description("Delete an existing tag")
    fun create(player: Player, tag: Tag) {
        TagService.unregisterTag(tag)
        player.sendMessage("${CC.B_GREEN}Unregistered tag with name ${tag.name}.")
    }

    @Subcommand("reset-template")
    @Description("Reset tags menu template")
    fun reset(player: Player) {
        TagService.template = TagsMenu()
        TagService.saveTemplate()
        player.sendMessage("${CC.B_GREEN}Successfully reset the tag template menu.")
    }

    @Subcommand("edit-template")
    @Description("Edit tags menu template")
    fun edit(player: Player) {
        EditTemplateLayoutMenu(TagService.template).openMenu(player)
    }

    @Subcommand("toggle")
    @Description("Toggle if a tag is enabled")
    fun toggle(sender: CommandSender, tag: Tag) {
        tag.enabled = !tag.enabled
        tag.saveUpdates()
        sender.sendMessage("${CC.SEC}You have ${TextUtil.stringifyBoolean(tag.enabled, TextUtil.FormatType.ENABLED_DISABLED).lowercase()} the tag ${tag.displayName}${CC.SEC}.")
    }

    @Subcommand("order")
    @Description("Update order for a tag")
    fun order(sender: CommandSender, tag: Tag, order: Int) {
        tag.order = order
        tag.saveUpdates()
        sender.sendMessage("${CC.SEC}You have updated the order of tag ${tag.displayName}${CC.SEC} to ${CC.PRI}${tag.order}${CC.SEC}.")
    }

    @Subcommand("component text")
    @Description("Update component text for a tag")
    fun componenetText(sender: CommandSender, tag: Tag, text: String) {
        tag.chatComponent.value = CC.translate(text)
        tag.saveUpdates()
        sender.sendMessage("${CC.SEC}You have updated the text component of tag ${tag.displayName}${CC.SEC} to ${CC.PRI}${text}${CC.SEC}.")
    }

    @Subcommand("component hover")
    @Description("Update component hover for a tag")
    fun componenetHover(sender: CommandSender, tag: Tag, text: String) {
        tag.chatComponent.hoverMessage = CC.translate(text)
        tag.saveUpdates()
        sender.sendMessage("${CC.SEC}You have updated the hover component of tag ${tag.displayName}${CC.SEC} to ${CC.PRI}${text}${CC.SEC}.")
    }

    @Subcommand("component click")
    @Description("Update component click for a tag")
    fun componenetClick(sender: CommandSender, tag: Tag, action: Action, text: String) {
        tag.chatComponent.clickEvent = ClickEvent(action, CC.translate(text))
        tag.saveUpdates()
        sender.sendMessage("${CC.SEC}You have updated the click component of tag ${tag.displayName}${CC.SEC} to ${CC.PRI}${text}${CC.SEC} with action ${CC.PRI}${action.name}${CC.SEC}.")
    }

    @Subcommand("permission|perm")
    @Description("Update permission for a tag")
    fun permission(sender: CommandSender, tag: Tag, permission: String) {
        tag.permission = permission
        tag.saveUpdates()
        sender.sendMessage("${CC.SEC}You have updated the permission of tag ${tag.displayName}${CC.SEC} to ${CC.PRI}${tag.permission}${CC.SEC}.")
    }

    @Subcommand("list")
    @Description("List all created tags")
    fun list(sender: CommandSender, @Default("1") page: Int) {
        TagPaginatedResult.display(sender, TagService.tags, page)
    }
}