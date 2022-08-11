package dev.foraged.cosmetics.configuration

import xyz.mkotb.configapi.comment.HeaderComment


@HeaderComment("Configuration for cosmetics")
class CosmeticConfiguration {
    var enableTags: Boolean = true
    var showTagsInChat: Boolean = true
    var tagOrder: Int = 50

    var enableChatColors: Boolean = true
    var enabledChatColors = mutableListOf(
        "RED", "YELLOW", "GOLD", "GREEN",
        "LIGHT_PURPLE", "BLUE", "GRAY",
        "AQUA", "DARK_AQUA", "WHITE"
    )
}