package dev.foraged.cosmetics.menu

import dev.foraged.cosmetics.tag.Tag
import dev.foraged.cosmetics.tag.TagService
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.template.MenuTemplate
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Type

class TagsMenu : MenuTemplate<Tag>(id = "tags") {

    override fun createEntryButton(entry: Tag): Button {
        return TagButton(entry)
    }

    override fun getListEntries(): List<Tag> {
        return TagService.tags.filter { it.enabled }.sortedBy { it.order }
    }

    inner class TagButton(val tag: Tag) : Button() {
        override fun getName(player: Player): String {
            return tag.displayName
        }

        override fun getDescription(player: Player): List<String> {
            return mutableListOf<String>().also { desc ->

                if (tag.permission.isNotEmpty() && !player.hasPermission(tag.permission)) {
                    desc.add("${CC.RED}You do not have permission to equip this tag.")
                } else {
                    desc.add("${ChatColor.YELLOW}Click to equip this tag.")
                }
            }
        }


        override fun getButtonItem(player: Player): ItemStack {
            return ItemBuilder.copyOf(tag.icon).name(getName(player)).setLore(getDescription(player)).build()
        }

        override fun clicked(player: Player, slot: Int, clickType: ClickType, view: InventoryView)
        {
            if (clickType.isLeftClick) {
                if (tag.permission.isNotEmpty()) {
                    if (!player.hasPermission(tag.permission)) {
                        player.sendMessage("${CC.RED}You do not have permission to equip this tag!")
                        return
                    }
                }

                tag.apply(player)
                player.sendMessage("${CC.SEC}You have equipped the ${tag.displayName}${CC.SEC}.")
            }
        }
    }

    override fun getAbstractType(): Type {
        return TagsMenu::class.java
    }
}