package cc.worldmandia.configuration.data

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val menuCommandName: String = "menu",
    val menuTitle: String = "&2Default title",
    val prevPage: String = "&bPrev Page",
    val nextPage: String = "&bNext Page",
    val page: String = "&9&lPage",
    val durationInSeconds: Long = 60,
    val saveDataEveryXMinutes: Long = 5,
    val commandsRegex: String = "/(?:msg|plugins)(?:\\s+\\S+)*"
)
