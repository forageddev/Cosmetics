package dev.foraged.cosmetics.menu

import com.cryptomorin.xseries.XMaterial
import dev.foraged.cosmetics.CosmeticsExtendedPlugin
import dev.foraged.cosmetics.configuration.CosmeticConfiguration
import dev.foraged.cosmetics.map.color.ChatColorPersistMap
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

class ChatColorMenu : MenuTemplate<ChatColor>(id = "chatColor") {

    override fun createEntryButton(entry: ChatColor): Button {
        return ChatColorButton(entry)
    }

    override fun getListEntries(): List<ChatColor> {
        return CosmeticsExtendedPlugin.instance.config<CosmeticConfiguration>().enabledChatColors.map(ChatColor::valueOf)
    }

    inner class ChatColorButton(val entry: ChatColor) : Button() {
        override fun getName(player: Player): String {
            return entry.toString() + entry.name.replace("_", " ").lowercase().capitalize()
        }

        override fun getDescription(player: Player): List<String> {
            return mutableListOf<String>().also { desc ->

                if (entry != ChatColor.WHITE && !player.hasPermission("cosmetics.color.${entry.name.lowercase()}")) {
                    desc.add("${CC.RED}You do not have permission to equip this chat color.")
                } else {
                    desc.add("${ChatColor.YELLOW}Click to equip this chat color.")
                }
            }
        }


        override fun getButtonItem(player: Player): ItemStack {
            return ItemBuilder.of(XMaterial.WHITE_WOOL).name(getName(player)).setLore(getDescription(player)).build()
        }

        override fun clicked(player: Player, slot: Int, clickType: ClickType, view: InventoryView)
        {
            if (clickType.isLeftClick) {
                if (entry != ChatColor.WHITE) {
                    if (!player.hasPermission("cosmetics.color.${entry.name.lowercase()}")) {
                        player.sendMessage("${CC.RED}You do not have permission to equip this color!")
                        return
                    }
                }

                ChatColorPersistMap[player.uniqueId] = entry
                player.sendMessage("${CC.SEC}You have equipped the ${getName(player)}${CC.SEC} chat color.")
            }
        }
    }

    override fun getAbstractType(): Type {
        return ChatColorMenu::class.java
    }
}