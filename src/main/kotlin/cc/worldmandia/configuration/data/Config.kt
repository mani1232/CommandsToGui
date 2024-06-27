package cc.worldmandia.configuration.data

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val menuCommandName: String = "menu",
    val menuTitle: String = "§2Default title",
    val prevPage: String = "§bPrev Page",
    val nextPage: String = "§bNext Page",
    val guiInCooldown: String = "guiInCooldown",
    val resetButton: CustomButton? = CustomButton("Reset", "COMPASS", "/cmd reset", mutableListOf("Lore1", "Lore2")),
    val cooldownInTicks: Long = 1200,
    val saveDataEveryXTicks: Long = 6000,
    val commandsRegex: String? = "/(?:msg|plugins)(?:\\s+\\S+)*"
)

@Serializable
data class CustomButton(
    val displayName: String,
    val material: String,
    val command: String,
    val lore: List<String>,
)