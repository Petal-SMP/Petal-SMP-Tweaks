package com.macuguita.petal_smp.common.attachments

import com.macuguita.petal_smp.common.PetalSMPTweaks.id
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentType

@Suppress("UnstableApiUsage")
object PetalAttachedTypes {
    val STARTER_ITEMS_ATTACHED_DATA: AttachmentType<GivenStarterItemsAttachedData> =
        AttachmentRegistry.create(
            id("starter_items_given")
        ) { builder ->
            builder
                .initializer { GivenStarterItemsAttachedData.DEFAULT }
                .persistent(GivenStarterItemsAttachedData.CODEC)
                .copyOnDeath()
        }
}