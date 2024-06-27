package cc.worldmandia

import cc.worldmandia.commands.guiCommand
import cc.worldmandia.commands.mainCtgCommand
import cc.worldmandia.configuration.ConfigUtils
import cc.worldmandia.configuration.data.Config
import cc.worldmandia.configuration.data.DataSave
import cc.worldmandia.events.CommandListeners
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import net.axay.kspigot.main.KSpigot
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.scheduler.BukkitRunnable
import java.io.File


class CommandsToGui : KSpigot() {

    private var adventure: BukkitAudiences? = null

    companion object {
        private lateinit var configFile: File
        private lateinit var dataFile: File
        private lateinit var pluginFolder: File

        lateinit var commandRegex: Regex
        lateinit var plugin: CommandsToGui
    }

    override fun load() {
        plugin = this
        CommandAPI.onLoad(CommandAPIBukkitConfig(plugin).silentLogs(true))
        pluginFolder = dataFolder
        configFile = File(pluginFolder, "modernConfig.yml")
        dataFile = File(pluginFolder, "data.yml")

        saveDefaultConfig()

        ConfigUtils.config = ConfigUtils.load(configFile, Config())
        ConfigUtils.dataSave = ConfigUtils.load(dataFile, DataSave())
    }

    override fun startup() {
        this.adventure = BukkitAudiences.create(this)
        CommandAPI.onEnable()

        UpdateFileTask().runTaskTimerAsynchronously(
            plugin,
            ConfigUtils.config.saveDataEveryXTicks,
            ConfigUtils.config.saveDataEveryXTicks
        )

        if (ConfigUtils.config.commandsRegex != null) {
            commandRegex = Regex(ConfigUtils.config.commandsRegex!!)
            plugin.server.pluginManager.registerEvents(CommandListeners(), this)
        }

        guiCommand()
        mainCtgCommand()
    }

    override fun shutdown() {
        ConfigUtils.update(dataFile, ConfigUtils.dataSave)
        CommandAPI.onDisable()
        this.adventure?.close()
    }

    class UpdateFileTask : BukkitRunnable() {
        override fun run() {
            ConfigUtils.update(dataFile, ConfigUtils.dataSave)
        }

    }

}