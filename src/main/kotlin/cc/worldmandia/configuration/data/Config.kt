package cc.worldmandia.configuration.data

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val menuCommandName: String = "menu",
    val durationInSeconds: Long = 60,
    val saveDataEveryXMinutes: Long = 5,
    val commandsRegex: String = "/(?:msg|plugins)(?:\\s+\\S+)*"
)
