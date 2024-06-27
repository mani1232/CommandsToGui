package cc.worldmandia.events

import cc.worldmandia.CommandsToGui
import cc.worldmandia.configuration.ConfigUtils
import cc.worldmandia.configuration.data.DataSave
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class CommandListeners : Listener {

    @EventHandler
    fun onPlayerCommandPreprocessEvent(event: PlayerCommandPreprocessEvent) {
        if (CommandsToGui.commandRegex.matches(event.message)) {
            ConfigUtils.dataSave.playerData.getOrPut(event.player.uniqueId.toString()) { mutableListOf() }
                .add(event.message)
            ConfigUtils.dataSave.commandsData.putIfAbsent(event.message, DataSave.CustomGuiItem())
        }
    }

}