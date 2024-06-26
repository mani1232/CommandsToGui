package cc.worldmandia

import cc.worldmandia.CommandsToGui.Companion.plugin
import com.mattmx.ktgui.dsl.event
import com.mattmx.ktgui.utils.legacyColor
import org.bukkit.event.player.PlayerCommandPreprocessEvent

fun commandInputEvent() = event<PlayerCommandPreprocessEvent>(plugin) {
    player.sendMessage("&7Player &c${player.name} use $message".legacyColor())
}