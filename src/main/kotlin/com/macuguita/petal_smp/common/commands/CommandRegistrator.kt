package com.macuguita.petal_smp.common.commands

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.commands.CommandSourceStack

interface CommandRegistrator {
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>)
}
