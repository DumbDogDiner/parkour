package com.dumbdogdiner.parkour.commands

import com.dumbdogdiner.parkour.ParkourPlugin
import com.dumbdogdiner.parkour.courses.Course
import com.dumbdogdiner.parkour.players.EditingSession
import com.dumbdogdiner.parkour.utils.Language
import com.dumbdogdiner.parkour.utils.Utils
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import kotlin.reflect.typeOf

class ParkourCommand : TabExecutor {
    private val plugin = ParkourPlugin.instance

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("parkour.command")) {
            sender.sendMessage(Language.noPermission)
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage(Language.unknownSubCommand)
            return true
        }

        when (args[0]) {
            "list" -> list(sender)
            "create" -> create(sender)
            "delete" -> delete(sender, args)
            else -> sender.sendMessage(Language.unknownSubCommand)
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        if (!sender.hasPermission("parkour.command")) {
            return mutableListOf()
        }

        return when {
            args.size < 2 -> mutableListOf("list", "edit", "create", "delete")
            args.size == 2 -> mutableListOf()
            else -> mutableListOf()
        }
    }


    private fun list(sender: CommandSender) {
        sender.sendMessage(Language.listCount.replace("%COUNT%", plugin.courseManager.getCourses().size.toString(), true));
        // var messages = plugin.courseManager.getCourses().map { it -> String.format("")}
    }

    private fun create(sender: CommandSender) {
        if (sender !is Player) {
            sender.sendMessage(Language.noConsole)
            return
        }

        plugin.sessionManager.createEditingSession(sender, Course(), EditingSession.Type.CREATE)
    }

    private fun delete(sender: CommandSender, args: Array<out String>) {
        if (sender !is Player) {
            sender.sendMessage(Language.noConsole)
            return
        }

        if (args.size < 2 || args[1].toIntOrNull() == null) {
            sender.sendMessage(Language.invalidCommandUsage.replace("%USAGE%", "/parkour delete <id>", true))
            return;
        }

        // TODO: Delete course
    }
}