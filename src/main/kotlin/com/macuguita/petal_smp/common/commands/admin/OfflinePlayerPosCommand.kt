package com.macuguita.petal_smp.common.commands.admin

import com.macuguita.petal_smp.common.commands.CommandRegistrator
import com.macuguita.petal_smp.common.commands.CommandResult
import com.macuguita.petal_smp.mixin.PlayerListAccessor
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.GameProfileArgument
import net.minecraft.core.BlockPos
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent


object OfflinePlayerPosCommand : CommandRegistrator {
    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            literal("offlineplayerpos")
                .requires { it.hasPermission(2) }
                .then(
                    argument("player", GameProfileArgument.gameProfile())
                        .executes { ctx ->
                            val player = GameProfileArgument.getGameProfiles(ctx, "player").singleOrNull()
                                ?: return@executes CommandResult.ERROR.value
                            val server = ctx.source.server

                            if (server.playerList.getPlayer(player.getId()) != null) {
                                ctx.source.sendFailure(Component.literal("The player has to be offline"))
                                return@executes CommandResult.ERROR.value
                            }

                            val handler = (server.playerList as PlayerListAccessor).`petal$getPlayerIo`()
                            val nbt = handler.`petal$getNbt`(player.id)

                            val list = nbt.getList("Pos", Tag.TAG_DOUBLE.toInt())

                            val x: Double = list.getDouble(0)
                            val y: Double = list.getDouble(1)
                            val z: Double = list.getDouble(2)

                            val blockPos = BlockPos(x.toInt(), y.toInt(), z.toInt())

                            val text: Component? = Component.literal("${player.name} was last seen at ")
                                .append(
                                    Component.literal("[${blockPos.x}, ${blockPos.y}, ${blockPos.z}]")
                                        .withStyle({
                                            it
                                                .withClickEvent(
                                                    ClickEvent(
                                                        ClickEvent.Action.RUN_COMMAND,
                                                        "/tp ${blockPos.x} ${blockPos.y} ${blockPos.z}"
                                                    )
                                                )
                                                .withColor(ChatFormatting.GREEN)
                                                .withHoverEvent(
                                                    HoverEvent(
                                                        HoverEvent.Action.SHOW_TEXT,
                                                        Component.literal("Click to be teleported")
                                                    )
                                                )
                                        })
                                )

                            ctx.source.sendSuccess({ text }, false)

                            return@executes CommandResult.SUCCESS.value
                        })

        )
    }
}
