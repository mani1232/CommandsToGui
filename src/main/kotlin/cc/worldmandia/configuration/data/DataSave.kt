package cc.worldmandia.configuration.data

import kotlinx.serialization.Serializable
import org.bukkit.Material

@Serializable
data class DataSave(
    // Command to CustomItem
    val commandsData: MutableMap<String, CustomGuiItem> = mutableMapOf(),
    // UUID of player to command
    val playerData: MutableMap<String, MutableList<String>> = mutableMapOf(),
) {
    @Serializable
    data class CustomGuiItem(
        var displayName: String? = null,
        var itemLore: MutableList<String>? = null,
        var material: String = Material.STONE.toString(),
        var commandExecuteType: CommandExecuteType = CommandExecuteType.CONSOLE,
    )

    enum class CommandExecuteType {
        CONSOLE,
        PLAYER
    }
}
