package com.macuguita.petal_smp.common.commands.tpa

import com.macuguita.petal_smp.common.PetalSMPTweaks
import com.macuguita.petal_smp.common.commands.CommandRegistrator
import com.macuguita.petal_smp.common.commands.CommandResult
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer

object TpaAcceptCommand: CommandRegistrator {
    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            literal("tpaaccept")
                .executes {
                    val player = it.source.playerOrException
                    val req = TpaManager.popMostRecent(
                        player.uuid,
                        player.level().gameRules.getRule(PetalSMPTweaks.REQUEST_EXPIRY_MS)?.get()
                    )

                    if (req == null) {
                        it.source.sendFailure(Component.literal("You have no pending requests"))
                        return@executes CommandResult.ERROR.value
                    }

                    executeTeleport(player, req)
                    CommandResult.SUCCESS.value
                }
                .then(
                    argument("player", EntityArgument.player())
                        .executes {
                            val player = it.source.playerOrException
                            val requester = EntityArgument.getPlayer(it, "player")

                            val req = TpaManager.popFromRequester(
                                player.uuid, requester.uuid,
                                player.level().gameRules.getRule(PetalSMPTweaks.REQUEST_EXPIRY_MS)?.get()
                            )

                            if (req == null) {
                                it.source.sendFailure(Component.literal("You have no request from that player"))
                                return@executes CommandResult.ERROR.value
                            }

                            executeTeleport(player, req)
                            CommandResult.SUCCESS.value
                        }
                )
        )
    }

    private fun executeTeleport(target: ServerPlayer, req: TpaRequest) {
        val server = target.server ?: return
        val requester = server.playerList.getPlayer(req.requester) ?: return

        val targetName = target.gameProfile.name
        val requesterName = requester.gameProfile.name

        when (req.type) {
            TpaType.TO -> {
                requester.stopRiding()
                requester.teleportTo(
                    target.serverLevel(),
                    target.x,
                    target.y,
                    target.z,
                    target.yRot,
                    target.xRot
                )

                target.sendSystemMessage(Component.literal("$requesterName has teleported to you"))
                requester.sendSystemMessage(Component.literal("You have teleported to $targetName"))
            }

            TpaType.HERE -> {
                target.stopRiding()
                target.teleportTo(
                    requester.serverLevel(),
                    requester.x,
                    requester.y,
                    requester.z,
                    requester.yRot,
                    requester.xRot
                )

                requester.sendSystemMessage(Component.literal("$targetName has teleported to you"))
                target.sendSystemMessage(Component.literal("You have teleported to $requesterName"))
            }
        }
    }
}

