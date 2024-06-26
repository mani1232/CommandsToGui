package cc.worldmandia.guis

import cc.worldmandia.CommandsToGui.Companion.plugin
import cc.worldmandia.configuration.ConfigUtils
import cc.worldmandia.configuration.data.DataSave
import com.mattmx.ktgui.components.screen.GuiMultiPageScreen
import com.mattmx.ktgui.dsl.button
import com.mattmx.ktgui.utils.not
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.util.*
import java.util.concurrent.TimeUnit


class MultiPageExample(player: Player) : GuiMultiPageScreen(!ConfigUtils.config.menuTitle, 6, maxPages = 5) {

    private val cooldownList = mutableListOf<UUID>()

    init {
        ConfigUtils.dataSave.playerData[player.uniqueId.toString()]?.forEach { commandName ->
            val item =
                ConfigUtils.dataSave.commandsData[commandName] ?: throw Exception("Command $commandName not found")
            this += button(Material.getMaterial(item.material) ?: Material.STONE) {
                click {
                    any {
                        if (!cooldownList.contains(player.uniqueId) || player.hasPermission("ctg.bypass")) {
                            plugin.server.dispatchCommand(
                                if (item.commandExecuteType == DataSave.CommandExecuteType.PLAYER) player else plugin.server.consoleSender,
                                if (item.commandExecuteType == DataSave.CommandExecuteType.CONSOLE) commandName.substring(
                                    1
                                ) else commandName
                            )
                            cooldownList.add(player.uniqueId)
                            plugin.server.asyncScheduler.runDelayed(plugin, {
                                cooldownList.remove(player.uniqueId)
                            }, ConfigUtils.config.durationInSeconds, TimeUnit.SECONDS)
                        } else {
                            player.sendMessage(!"You need wait 60 sec")
                        }
                    }
                }
                if (item.displayName != null) {
                    named(!item.displayName!!)
                }
                if (item.itemLore != null) {
                    lore(!item.itemLore!!)
                }
            }
        }
        button(Material.GRAY_STAINED_GLASS_PANE) {
            named(!" ") slots (0..8).toList() + (45..53).toList()
        }
        val currentPage =
            button(Material.COMPASS) { named(!"${ConfigUtils.config.page} ${getCurrentPage()}") slot 49 }
        button(Material.ARROW) {
            click {
                ClickType.LEFT {
                    gotoNextPage(player)
                    currentPage named !"${ConfigUtils.config.page} ${getCurrentPage()}"
                    currentPage.update(player)
                }
            }
            named(!ConfigUtils.config.nextPage) slot 53
        }
        button(Material.ARROW) {
            click {
                ClickType.LEFT {
                    gotoPrevPage(player)
                    currentPage named !"${ConfigUtils.config.page} ${getCurrentPage()}"
                    currentPage.update(player)
                }
            }
            named(!ConfigUtils.config.prevPage) slot 45
        }
    }
}