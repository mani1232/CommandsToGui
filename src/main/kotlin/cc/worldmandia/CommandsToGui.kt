package cc.worldmandia

import cc.worldmandia.commands.editItemCommand
import cc.worldmandia.commands.guiCommand
import cc.worldmandia.configuration.ConfigUtils
import cc.worldmandia.configuration.data.Config
import cc.worldmandia.configuration.data.DataSave
import cc.worldmandia.events.commandInputEvent
import com.mattmx.ktgui.GuiManager
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.io.File


class CommandsToGui : JavaPlugin() {

    private var adventure: BukkitAudiences? = null

    companion object {
        private lateinit var configFile: File
        private lateinit var dataFile: File
        private lateinit var pluginFolder: File

        lateinit var commandRegex: Regex
        lateinit var plugin: CommandsToGui
    }

    override fun onLoad() {
        plugin = this
        pluginFolder = dataFolder
        configFile = File(pluginFolder, "modernConfig.yml")
        dataFile = File(pluginFolder, "data.yml")

        saveDefaultConfig()

        ConfigUtils.config = ConfigUtils.load(configFile, Config())
        ConfigUtils.dataSave = ConfigUtils.load(dataFile, DataSave())

        commandRegex = Regex(ConfigUtils.config.commandsRegex)
    }

    override fun onEnable() {
        this.adventure = BukkitAudiences.create(this)

        UpdateFileTask().runTaskTimerAsynchronously(plugin, ConfigUtils.config.saveDataEveryXTicks, ConfigUtils.config.saveDataEveryXTicks)
        GuiManager.init(this)
        GuiManager.guiConfigManager.setConfigFile<CommandsToGui>(config)

        commandInputEvent()
        guiCommand()
        editItemCommand()
    }

    override fun onDisable() {
        this.adventure?.close()
    }

    class UpdateFileTask: BukkitRunnable() {
        override fun run() {
            ConfigUtils.update(dataFile, ConfigUtils.dataSave)
        }

    }

}