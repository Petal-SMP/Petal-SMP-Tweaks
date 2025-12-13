package com.macuguita.petal_smp.common.commands

import com.macuguita.petal_smp.common.commands.admin.OfflinePlayerPosCommand
import com.macuguita.petal_smp.common.commands.admin.OfflineTpCommand
import com.macuguita.petal_smp.common.commands.spawn.SpawnCommand
import com.macuguita.petal_smp.common.commands.tpa.TpaAcceptCommand
import com.macuguita.petal_smp.common.commands.tpa.TpaCommand
import com.macuguita.petal_smp.common.commands.tpa.TpaHereCommand
import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

interface CommandRegistrator {
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>)

    class RegisterCommands : CommandRegistrationCallback {
        override fun register(
            dispatcher: CommandDispatcher<CommandSourceStack>,
            registryAccess: CommandBuildContext,
            environment: Commands.CommandSelection
        ) {
            OfflinePlayerPosCommand.register(dispatcher)
            OfflineTpCommand.register(dispatcher)
            SpawnCommand.register(dispatcher)
            TpaCommand.register(dispatcher)
            TpaHereCommand.register(dispatcher)
            TpaAcceptCommand.register(dispatcher)
        }
    }
}
