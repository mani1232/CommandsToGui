package cc.worldmandia.commands

import cc.worldmandia.CommandsToGui.Companion.configFile
import cc.worldmandia.configuration.ConfigUtils
import cc.worldmandia.configuration.data.Config
import cc.worldmandia.configuration.data.DataSave
import cc.worldmandia.guis.MultiPageGui
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.kotlindsl.*
import org.bukkit.entity.Player

fun guiCommand() = commandAPICommand(ConfigUtils.config.menuCommandName) {
    permission = CommandPermission.fromString("ctg.command.gui")
    playerExecutor { player, _ ->
        MultiPageGui(player)
    }
}

fun mainCtgCommand() = commandAPICommand("ctg") {
    permission = CommandPermission.fromString("ctg.command.ctg")
    reloadCommand()
    editCommand()
    forceCommand()
}

fun reloadCommand() = subcommand("reload") {
    permission = CommandPermission.fromString("ctg.command.reload")

    anyExecutor { commandSender, _ ->
        ConfigUtils.config = ConfigUtils.load(configFile, Config())
        commandSender.sendMessage("Reloaded only config")
    }
}

fun editCommand() = subcommand("editItem") {
    permission = CommandPermission.fromString("ctg.command.editItem")
    multiLiteralArgument(
        nodeName = "command",
        *(if (ConfigUtils.dataSave.commandsData.keys.isNotEmpty()) ConfigUtils.dataSave.commandsData.keys.map {
            it.replace(
                " ",
                "|"
            )
        } else listOf(" ")).toTypedArray(),
        optional = false
    )
    multiLiteralArgument(nodeName = "type", *EditType.entries.map { it.name }.toTypedArray(), optional = false)
    greedyStringArgument("newValue", false)
    anyExecutor { commandSender, commandArguments ->
        val command: String = (commandArguments["command"] as String).replace("|", " ")
        val newValue: String = commandArguments["newValue"] as String
        val editType: EditType = EditType.valueOf(commandArguments["player"] as String)
        val commandData = ConfigUtils.dataSave.commandsData[command]
        if (commandData != null) {
            when (editType) {
                EditType.NAME -> {
                    commandData.displayName = newValue
                }

                EditType.LORE -> {
                    commandData.itemLore?.add(newValue)
                }

                EditType.MATERIAL -> {
                    commandData.material = newValue
                }

                EditType.EXECUTOR -> {
                    commandData.commandExecuteType = DataSave.CommandExecuteType.valueOf(newValue.uppercase())
                }
            }
            ConfigUtils.dataSave.commandsData[command] = commandData
            commandSender.sendMessage("Edit ${editType.name} for command: $command")
        } else {
            commandSender.sendMessage("Command not found")
        }
    }
}

fun forceCommand() = subcommand("force") {
    permission = CommandPermission.fromString("ctg.command.force")
    playerArgument("player", false)
    multiLiteralArgument(nodeName = "type", "add", "remove", optional = false)
    greedyStringArgument("command", false)

    anyExecutor { commandSender, commandArguments ->
        val newValue: String = commandArguments["command"] as String
        val type: String = commandArguments["type"] as String
        val player: Player = commandArguments["player"] as Player
        when (type) {
            "add" -> {
                if (!ConfigUtils.dataSave.playerData.getOrPut(player.uniqueId.toString()) { mutableListOf() }
                        .contains(newValue)) {
                    ConfigUtils.dataSave.playerData.getOrPut(player.uniqueId.toString()) { mutableListOf() }
                        .add(newValue)
                    commandSender.sendMessage("Force added")
                } else {
                    commandSender.sendMessage("Already have")
                }
                ConfigUtils.dataSave.commandsData.putIfAbsent(newValue, DataSave.CustomGuiItem())
            }

            "remove" -> {
                ConfigUtils.dataSave.playerData[player.uniqueId.toString()]?.remove(newValue)
                commandSender.sendMessage("Force removed")
            }

            else -> {
                commandSender.sendMessage("$type not found")
            }
        }
    }
}

enum class EditType {
    NAME,
    LORE,
    MATERIAL,
    EXECUTOR
}