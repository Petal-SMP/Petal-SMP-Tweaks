package com.macuguita.petal_smp.common.commands.tpa

import java.util.*

enum class TpaType {
    TO,
    HERE
}

data class TpaRequest(
    val requester: UUID,
    val target: UUID,
    val type: TpaType,
    val timestamp: Long
)
