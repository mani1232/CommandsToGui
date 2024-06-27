package cc.worldmandia.guis

import cc.worldmandia.CommandsToGui.Companion.plugin
import cc.worldmandia.configuration.ConfigUtils
import cc.worldmandia.configuration.data.DataSave
import cc.worldmandia.configuration.data.DataSave.CustomGuiItem
import net.axay.kspigot.chat.literalText
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.items.setLore
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
            title = literalText(ConfigUtils.config.menuTitle)

            page(1) {
                val compound = createRectCompound<Pair<String, CustomGuiItem>>(
                    Slots.RowOneSlotOne, Slots.RowSixSlotEight,
                    iconGenerator = {
                        val item = ItemStack(Material.getMaterial(it.second.material) ?: Material.STONE)
                        val meta = item.itemMeta!!
                        meta.setDisplayName(it.second.displayName)
                        meta.setLore {
                            it.second.itemLore?.unaryPlus()
                        }
                        item.setItemMeta(meta)
                        item
                    },
                    onClick = { clickEvent, element ->
                        clickEvent.bukkitEvent.isCancelled = true
                        if (!cooldownList.contains(player.uniqueId) || player.hasPermission("ctg.bypass")) {
                            plugin.server.dispatchCommand(
                                if (element.second.commandExecuteType == DataSave.CommandExecuteType.PLAYER) player else plugin.server.consoleSender,
                                if (element.second.commandExecuteType == DataSave.CommandExecuteType.CONSOLE) element.first.substring(
                                    1
                                ) else element.first
                            )
                            cooldownList.add(player.uniqueId)
                            RemoveTask(player).runTaskLaterAsynchronously(plugin, ConfigUtils.config.cooldownInTicks)
                        }
                    }
                )
                compoundScroll(
                    Slots.RowOneSlotNine,
                    ItemStack(Material.PAPER), compound, scrollTimes = 6
                )
                compoundScroll(
                    Slots.RowSixSlotNine,
                    ItemStack(Material.PAPER), compound, scrollTimes = 6, reverse = true
                )
                compound.addContent(ConfigUtils.dataSave.commandsData.map { it.key to it.value })
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