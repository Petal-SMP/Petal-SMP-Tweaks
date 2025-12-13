package com.macuguita.petal_smp.common.commands.spawn

import com.macuguita.petal_smp.common.commands.CommandRegistrator
import com.macuguita.petal_smp.common.commands.CommandResult
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.literal
import net.minecraft.world.phys.Vec3

object SpawnCommand : CommandRegistrator {
    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            literal("spawn")
                .executes {
                    val source = it.source
                    val player = source.player ?: return@executes CommandResult.ERROR.value
                    val overworld = source.server.overworld()
                    val spawnPos: Vec3 = Vec3.atBottomCenterOf(overworld.sharedSpawnPos)

                    player.teleportTo(
                        overworld,
                        spawnPos.x,
                        spawnPos.y,
                        spawnPos.z,
                        player.yRot,
                        player.xRot
                    )

                    return@executes CommandResult.SUCCESS.value
                }
        )
    }
}
