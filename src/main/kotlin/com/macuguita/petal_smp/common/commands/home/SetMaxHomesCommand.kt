package com.macuguita.petal_smp.common.commands.home

import com.macuguita.petal_smp.common.attachments.Homes
import com.macuguita.petal_smp.common.commands.CommandRegistrator
import com.macuguita.petal_smp.common.commands.CommandResult
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.EntityArgument

object SetMaxHomesCommand : CommandRegistrator {
    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            literal("setmaxhomes")
                .requires { it.hasPermission(2) }
            .then(
                Commands.argument("player", EntityArgument.player())
                    .then(
                        Commands.argument("amount", IntegerArgumentType.integer())
                            .executes { ctx ->
                            val player = EntityArgument.getPlayer(ctx, "player")
                            val amount = IntegerArgumentType.getInteger(ctx, "amount")
                            Homes.get(player).maxHomes = amount
                            CommandResult.SUCCESS.value
                        }
                    )
            )
        )
    }
}
