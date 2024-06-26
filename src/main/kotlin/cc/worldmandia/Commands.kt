package cc.worldmandia

import com.mattmx.ktgui.commands.rawCommand
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

fun guiCommand() = rawCommand("testCmd") {
    permission = "ktgui.command"
    playerOnly = true
    suggestSubCommands = true
    cooldown(60.seconds.toJavaDuration())

    executes {
        MultiPageExample().open(player)
    }
}.register(false)