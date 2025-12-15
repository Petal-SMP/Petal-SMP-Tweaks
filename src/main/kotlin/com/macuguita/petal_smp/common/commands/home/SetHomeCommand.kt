package com.macuguita.petal_smp.common.commands.home

import com.macuguita.petal_smp.common.attachments.HomeData
import com.macuguita.petal_smp.common.attachments.Homes
import com.macuguita.petal_smp.common.commands.CommandRegistrator
import com.macuguita.petal_smp.common.commands.CommandResult
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.Commands.literal
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer

object SetHomeCommand : CommandRegistrator {
    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            literal("sethome")
                .then(
                    Commands.argument("name", StringArgumentType.word())
                        .executes { ctx ->
                            val player = ctx.source.playerOrException
                            val name = StringArgumentType.getString(ctx, "name")
                            return@executes CommandResult.fromBoolean(addHome(player, name)).value
                        }
                )
                .executes { ctx ->
                    val player = ctx.source.playerOrException
                    return@executes CommandResult.fromBoolean(addHome(player, "home")).value
                }
        )
    }

    private fun addHome(player: ServerPlayer, name: String): Boolean {
        val homeData: HomeData = Homes.get(player)
        val n = name.lowercase()

        if (homeData.homes.size >= homeData.maxHomes || homeData.homes.any { it.name == n }) {
            player.sendSystemMessage(
                Component.literal("Cannot add home '$name'. Maximum homes reached or home already exists.")
                    .withStyle(ChatFormatting.RED)
            )
            return false
        }

        homeData.addHome(name, player.blockPosition())
        player.sendSystemMessage(Component.literal("Home '$name' set at your current position!"))
        return true
    }

}
