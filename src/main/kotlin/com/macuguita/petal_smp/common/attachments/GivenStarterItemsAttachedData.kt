package com.macuguita.petal_smp.common.attachments

import com.macuguita.petal_smp.common.attachments.PetalAttachedTypes.STARTER_ITEMS_ATTACHED_DATA
import com.mojang.serialization.Codec
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget

@Suppress("UnstableApiUsage")
data class GivenStarterItemsAttachedData(val givenPokeballs: Boolean) {
    companion object {
        val CODEC: Codec<GivenStarterItemsAttachedData> =
            Codec.BOOL.xmap(::GivenStarterItemsAttachedData, GivenStarterItemsAttachedData::givenPokeballs)

        val DEFAULT = GivenStarterItemsAttachedData(false)

        fun setStarterItems(player: AttachmentTarget, givenStarterItems: Boolean = true) {
            player.setAttached(STARTER_ITEMS_ATTACHED_DATA, GivenStarterItemsAttachedData(givenStarterItems))
        }

        fun getStarterItems(player: AttachmentTarget): Boolean =
            player.getAttachedOrElse(STARTER_ITEMS_ATTACHED_DATA, GivenStarterItemsAttachedData(false)).givenPokeballs
    }
}
