package com.macuguita.petal_smp.common.tpa.commands

import com.macuguita.petal_smp.common.tpa.TpaType
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.EntityArgument

object TpaCommand {
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            literal("tpa")
                .then(
                    argument("player", EntityArgument.player())
                        .executes {
                            handle(it, TpaType.TO)
                        }
                )
        )
    }
}

