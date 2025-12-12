package com.macuguita.petal_smp.common

import com.cobblemon.mod.common.CobblemonItems
import com.google.common.reflect.Reflection
import com.macuguita.petal_smp.common.attachments.GivenStarterItemsAttachedData
import com.macuguita.petal_smp.common.attachments.PetalAttachedTypes
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import org.apache.logging.log4j.core.jmx.Server
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object PetalSMPTweaks : ModInitializer {
    const val MOD_ID: String = "petal-smp-tweaks"
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    fun id(name: String) = ResourceLocation.fromNamespaceAndPath(MOD_ID, name)

    override fun onInitialize() {
        Reflection.initialize(PetalAttachedTypes::class.java)
        ServerPlayerEvents.JOIN.register { player ->
            if (!GivenStarterItemsAttachedData.getStarterItems(player)) {
                givePokeballs(player)
                givePokedex(player)
                GivenStarterItemsAttachedData.setStarterItems(player, true)
            }
        }
    }

    fun giveStacks(player: ServerPlayer, stack: ItemStack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, true);
        }
    }

    fun givePokeballs(player: ServerPlayer) {
        val pokeballs = numToPokeball(player.uuid.hashCode()%6, 10)
        giveStacks(player, pokeballs)
    }

    fun givePokedex(player: ServerPlayer) {
        val pokedex = numToPokedex(player.uuid.hashCode()%6)
        giveStacks(player, pokedex)
    }

    fun numToPokedex(num: Int): ItemStack {
        return when (num) {
            0 -> ItemStack(CobblemonItems.POKEDEX_RED)
            1 -> ItemStack(CobblemonItems.POKEDEX_YELLOW)
            2 -> ItemStack(CobblemonItems.POKEDEX_BLUE)
            3 -> ItemStack(CobblemonItems.POKEDEX_PINK)
            4 -> ItemStack(CobblemonItems.POKEDEX_BLACK)
            5 -> ItemStack(CobblemonItems.POKEDEX_WHITE)
            else -> ItemStack(CobblemonItems.POKEDEX_RED)
        }
    }

    fun numToPokeball(num: Int, quantity: Int = 1): ItemStack {
        return when (num) {
            0 -> ItemStack(CobblemonItems.POKE_BALL, quantity)
            1 -> ItemStack(CobblemonItems.CITRINE_BALL, quantity)
            2 -> ItemStack(CobblemonItems.AZURE_BALL, quantity)
            3 -> ItemStack(CobblemonItems.ROSEATE_BALL, quantity)
            4 -> ItemStack(CobblemonItems.SLATE_BALL, quantity)
            5 -> ItemStack(CobblemonItems.VERDANT_BALL, quantity)
            else -> ItemStack(CobblemonItems.POKE_BALL, quantity)
        }
    }
}