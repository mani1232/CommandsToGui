package cc.worldmandia

import cc.worldmandia.commands.guiCommand
import cc.worldmandia.events.commandInputEvent
import com.mattmx.ktgui.GuiManager
import org.bukkit.plugin.java.JavaPlugin

class CommandsToGui: JavaPlugin() {

    companion object {
        lateinit var plugin: JavaPlugin
    }

    override fun onLoad() {
        plugin = this
    }

    override fun onEnable() {
        saveDefaultConfig()
        GuiManager.init(this)
        GuiManager.guiConfigManager.setConfigFile<CommandsToGui>(config)
        commandInputEvent()
        guiCommand()
    }
}