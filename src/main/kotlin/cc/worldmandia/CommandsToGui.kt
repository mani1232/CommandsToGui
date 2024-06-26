package cc.worldmandia

import cc.worldmandia.commands.editItemCommand
import cc.worldmandia.commands.guiCommand
import cc.worldmandia.configuration.ConfigUtils
import cc.worldmandia.configuration.data.Config
import cc.worldmandia.configuration.data.DataSave
import cc.worldmandia.events.commandInputEvent
import com.mattmx.ktgui.GuiManager
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.concurrent.TimeUnit

class CommandsToGui : JavaPlugin() {

    private val configFile = File(dataFolder, "config.yml")
    private val dataFile = File(dataFolder, "data.yml")

    companion object {

        lateinit var commandRegex: Regex
        lateinit var plugin: CommandsToGui
    }

    override fun onLoad() {
        plugin = this

        saveDefaultConfig()

        ConfigUtils.config = ConfigUtils.load(configFile, Config())
        ConfigUtils.dataSave = ConfigUtils.load(dataFile, DataSave())

        commandRegex = Regex(ConfigUtils.config.commandsRegex)

        server.asyncScheduler.runAtFixedRate(plugin, {
            ConfigUtils.update(dataFile, ConfigUtils.dataSave)
        }, ConfigUtils.config.saveDataEveryXMinutes, ConfigUtils.config.saveDataEveryXMinutes, TimeUnit.MINUTES)
    }

    override fun onEnable() {
        GuiManager.init(this)
        GuiManager.guiConfigManager.setConfigFile<CommandsToGui>(config)

        commandInputEvent()
        guiCommand()
        editItemCommand()
    }
}