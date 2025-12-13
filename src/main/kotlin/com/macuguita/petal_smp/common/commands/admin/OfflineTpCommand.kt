package com.macuguita.petal_smp.common.commands.admin

import com.macuguita.petal_smp.common.commands.CommandRegistrator
import com.macuguita.petal_smp.common.commands.CommandResult
import com.macuguita.petal_smp.mixin.PlayerListAccessor
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.GameProfileArgument
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.nbt.DoubleTag
import net.minecraft.nbt.ListTag
import net.minecraft.network.chat.Component

object OfflineTpCommand : CommandRegistrator {
    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            literal("offlinetp")
                .requires { it.hasPermission(2) }
                .then(
                    argument("player", GameProfileArgument.gameProfile())
                        .then(
                            argument("pos", BlockPosArgument.blockPos())
                                .executes { ctx ->
                                    val player = GameProfileArgument.getGameProfiles(ctx, "player")
                                        .singleOrNull() ?: return@executes CommandResult.ERROR.value
                                    val pos = BlockPosArgument.getLoadedBlockPos(ctx, "pos")
                                    val server = ctx.source.server

                                    server.playerList.getPlayer(player.id)?.let {
                                        ctx.source.sendFailure(Component.literal("The player has to be offline"))
                                        return@executes CommandResult.ERROR.value
                                    }

                                    val handler = (server.playerList as PlayerListAccessor).`petal$getPlayerIo`()
                                    handler.`petal$edit`(player.id) {
                                        it.put(
                                            "Pos",
                                            newDoubleList(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
                                        )
                                        it.putString("Dimension", ctx.source.level.dimension().toString())
                                    }

                                    ctx.source.sendSuccess(
                                        { Component.literal("Teleported ${player.name} to ${pos.x}, ${pos.y}, ${pos.z}") },
                                        true
                                    )
                                    CommandResult.SUCCESS.value
                                }
                        )
                )
        )
    }

    fun newDoubleList(vararg numbers: Double): ListTag {
        val nbtList = ListTag()
        for (d in numbers) {
            nbtList.add(DoubleTag.valueOf(d))
        }

        return nbtList
    }
}
