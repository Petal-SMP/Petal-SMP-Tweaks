/*
 * Copyright (c) 2025 macuguita
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
