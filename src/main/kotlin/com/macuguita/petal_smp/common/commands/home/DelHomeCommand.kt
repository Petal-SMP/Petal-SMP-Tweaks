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

object DelHomeCommand : CommandRegistrator {
    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            literal("delhome")
                .then(
                    Commands.argument("name", StringArgumentType.word())
                        .suggests { context, builder -> HomeUtils.suggestHomes(context, builder) }
                        .executes { ctx ->
                            val player = ctx.source.playerOrException
                            val name = StringArgumentType.getString(ctx, "name")
                            CommandResult.fromBoolean(deleteHome(player, name)).value
                        }
                )
        )
    }

    private fun deleteHome(player: ServerPlayer, name: String): Boolean {
        val homeData: HomeData = Homes.get(player)
        val n = name.lowercase()
        val homeExists = homeData.homes.any { it.name == n }

        if (!homeExists) {
            player.sendSystemMessage(Component.literal("Home '$name' does not exist.").withStyle(ChatFormatting.RED))
            return false
        }

        homeData.removeHome(n)
        player.sendSystemMessage(Component.literal("Home '$name' has been removed."))
        return true
    }
}
