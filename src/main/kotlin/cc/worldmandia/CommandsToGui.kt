package cc.worldmandia

import cc.worldmandia.commands.editItemCommand
import cc.worldmandia.commands.guiCommand
import cc.worldmandia.configuration.ConfigUtils
import cc.worldmandia.events.commandInputEvent
import com.mattmx.ktgui.GuiManager
import org.bukkit.plugin.java.JavaPlugin

class CommandsToGui : JavaPlugin() {

    companion object {

        lateinit var commandRegex: Regex
        lateinit var plugin: CommandsToGui
    }

    override fun onLoad() {

        commandRegex = Regex("/(?:msg|plugins)(?:\\s+\\S+)*")
        plugin = this

        saveDefaultConfig()
        ConfigUtils.load()
    }

    override fun onEnable() {
        GuiManager.init(this)
        GuiManager.guiConfigManager.setConfigFile<CommandsToGui>(config)

        commandInputEvent()
        guiCommand()
        editItemCommand()
    }
}