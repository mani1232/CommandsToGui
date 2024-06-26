package cc.worldmandia.commands

import cc.worldmandia.configuration.ConfigUtils
import cc.worldmandia.configuration.data.DataSave
import cc.worldmandia.guis.MultiPageExample
import com.mattmx.ktgui.commands.declarative.argument
import com.mattmx.ktgui.commands.declarative.div
import com.mattmx.ktgui.commands.declarative.invoke
import com.mattmx.ktgui.commands.rawCommand
import com.mattmx.ktgui.conversation.refactor.conversation
import com.mattmx.ktgui.conversation.refactor.getEnum
import com.mattmx.ktgui.conversation.refactor.getString
import com.mattmx.ktgui.utils.not
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun guiCommand() = rawCommand(ConfigUtils.config.menuCommandName) {
    permission = "ctg.command.menu"
    playerOnly = true
    suggestSubCommands = true

    executes {
        MultiPageExample(player).open(player)
    }
}.register(false)

fun editItemCommand() = "ctg"<CommandSender> {
    buildAutomaticPermissions("ctg.command")
    val commandName by argument<String>("name", true)
    val player by argument<Player>("player")

    runs {
        sender.sendMessage("Main command")
    }

    subcommand<CommandSender>("force") {

        subcommand<CommandSender>("add" / player / commandName) {
            commandName suggests { ConfigUtils.dataSave.commandsData.keys.toList() }

            runs {
                val cmd = commandName()
                ConfigUtils.dataSave.playerData.getOrPut(player().uniqueId.toString()) { mutableListOf() }.add(cmd)
                ConfigUtils.dataSave.commandsData.putIfAbsent(cmd, DataSave.CustomGuiItem())
                sender.sendMessage("Force added")
            }
        }

        subcommand<CommandSender>("remove" / player / commandName) {
            commandName suggests { ConfigUtils.dataSave.commandsData.keys.toList() }

            runs {
                val cmd = commandName()
                ConfigUtils.dataSave.playerData.getOrPut(player().uniqueId.toString()) { mutableListOf() }.remove(cmd)
                sender.sendMessage("Force removed")
            }
        }

        runs {
            sender.sendMessage("Force command")
        }

    }

    subcommand<Player>("editItem" / commandName) {
        commandName suggests { ConfigUtils.dataSave.commandsData.keys.toList() }

        runs {
            val cmdName = commandName()
            if (ConfigUtils.dataSave.commandsData.containsKey(cmdName)) {
                val newItemData = DataSave.CustomGuiItem()
                conversation<Player> {
                    exitOn = "cancel"

                    exit {
                        ConfigUtils.dataSave.commandsData[cmdName] = newItemData
                    }

                    start {
                        sender.sendMessage("Start editing item...")
                    }

                    getString {
                        message = !"&7Please input a item name"
                        matches { result.isPresent }
                        runs {
                            newItemData.displayName = result.get()
                            conversable.sendMessage("&7Item name updated")
                        }
                    }

                    getString {
                        message = !"&7Please input a item lore"
                        matches { result.isPresent }
                        runs {
                            newItemData.itemLore = result.get()
                            conversable.sendMessage("&7Item lore updated")
                        }
                    }

                    getEnum<Material, Player> {
                        message = !"&7Please input a material"
                        matches { result.isPresent }
                        runs {
                            newItemData.material = result.get().toString()
                            conversable.sendMessage("&7Item material updated")
                        }
                    }

                    getEnum<DataSave.CommandExecuteType, Player> {
                        message = !"&7Please input a CommandExecuteType"
                        matches { result.isPresent }
                        runs {
                            newItemData.commandExecuteType = result.get()
                            conversable.sendMessage("&7Item CommandExecuteType updated")
                        }
                    }
                } timeout 60 begin sender
                ConfigUtils.dataSave.commandsData[cmdName] = newItemData
            }

        }
    }

}.register(false)