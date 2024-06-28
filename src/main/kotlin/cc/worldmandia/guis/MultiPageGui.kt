package cc.worldmandia.guis

import cc.worldmandia.CommandsToGui.Companion.plugin
import cc.worldmandia.configuration.ConfigUtils
import cc.worldmandia.configuration.data.DataSave
import cc.worldmandia.configuration.data.DataSave.CustomGuiItem
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.openGUI
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*


class MultiPageGui(player: Player) {

    companion object {
        private val cooldownList = mutableListOf<UUID>()
    }

    init {
        val gui = kSpigotGUI(GUIType.SIX_BY_NINE) {
            title = ConfigUtils.config.menuTitle

            page(1) {
                val compound = createRectCompound<Pair<String, CustomGuiItem>>(
                    Slots.RowOneSlotOne, Slots.RowSixSlotEight,
                    iconGenerator = {
                        val item = ItemStack(Material.getMaterial(it.second.material) ?: Material.STONE)
                        val meta = item.itemMeta!!
                        meta.setDisplayName(it.second.displayName)
                        meta.lore = it.second.itemLore ?: mutableListOf()
                        item.setItemMeta(meta)
                        item
                    },
                    onClick = { clickEvent, element ->
                        clickEvent.bukkitEvent.isCancelled = true
                        if (!cooldownList.contains(player.uniqueId) || player.hasPermission("ctg.bypass")) {
                            plugin.server.dispatchCommand(
                                if (element.second.commandExecuteType == DataSave.CommandExecuteType.PLAYER) player else plugin.server.consoleSender,
                                (if (element.second.commandExecuteType == DataSave.CommandExecuteType.CONSOLE) element.first.substring(
                                    1
                                ) else element.first).replace("[player]", player.name)
                            )
                            cooldownList.add(player.uniqueId)
                            RemoveTask(player).runTaskLaterAsynchronously(plugin, ConfigUtils.config.cooldownInTicks)
                        } else {
                            player.sendMessage(ConfigUtils.config.guiInCooldown)
                        }
                    }
                )
                val resetBtnData = ConfigUtils.config.resetButton

                if (resetBtnData != null) {
                    val resetBtn = ItemStack(Material.getMaterial(resetBtnData.material) ?: Material.COMPASS)
                    val meta = resetBtn.itemMeta!!
                    meta.setDisplayName(resetBtnData.displayName)
                    meta.lore = resetBtnData.lore
                    resetBtn.setItemMeta(meta)

                    button(Slots.RowThreeSlotNine, resetBtn) {
                        it.bukkitEvent.isCancelled = true
                        plugin.server.dispatchCommand(plugin.server.consoleSender, resetBtnData.command.replace("[player]", player.name))
                    }
                }

                val item = ItemStack(Material.ARROW)
                val meta = item.itemMeta!!
                meta.setDisplayName(ConfigUtils.config.nextPage)
                item.setItemMeta(meta)
                compoundScroll(
                    Slots.RowOneSlotNine,
                    item, compound, scrollTimes = 6
                )
                meta.setDisplayName(ConfigUtils.config.prevPage)
                item.setItemMeta(meta)
                compoundScroll(
                    Slots.RowSixSlotNine,
                    item, compound, scrollTimes = 6, reverse = true
                )
                compound.addContent(ConfigUtils.dataSave.commandsData.filter {
                    ConfigUtils.dataSave.playerData[player.uniqueId.toString()]?.contains(
                        it.key
                    ) ?: false
                }.map { it.key to it.value })
            }
        }
        player.openGUI(gui)
    }

    class RemoveTask(private val player: Player) : BukkitRunnable() {
        override fun run() {
            cooldownList.remove(player.uniqueId)
        }

    }
}