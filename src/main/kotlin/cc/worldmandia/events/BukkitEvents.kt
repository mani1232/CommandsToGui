package cc.worldmandia.events

import cc.worldmandia.CommandsToGui
import cc.worldmandia.CommandsToGui.Companion.plugin
import cc.worldmandia.configuration.ConfigUtils
import cc.worldmandia.configuration.data.DataSave
import com.mattmx.ktgui.dsl.event
import org.bukkit.event.player.PlayerCommandPreprocessEvent

fun commandInputEvent() = event<PlayerCommandPreprocessEvent>(plugin) {
    if (CommandsToGui.commandRegex.matches(message)) {
        ConfigUtils.dataSave.playerData.getOrPut(player.uniqueId.toString()) { mutableListOf() }.add(message)
        ConfigUtils.dataSave.commandsData.putIfAbsent(message, DataSave.CustomGuiItem())
    }
}