package com.macuguita.petal_smp.client

import com.mojang.blaze3d.vertex.PoseStack
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.minecraft.Util
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.TitleScreen
import net.minecraft.resources.ResourceLocation
import java.time.LocalDate
import kotlin.random.Random

object ClientEntrypoint : ClientModInitializer {

    override fun onInitializeClient() {
        if (!isHolidaySeason()) return

        ScreenEvents.BEFORE_INIT.register { _, screen, _, _ ->
            if (screen is TitleScreen) {
                generateSnowfallLayers()
                screenStartTime = Util.getMillis()

                ScreenEvents.afterRender(screen).register(::afterTitleScreenRender)
            }
        }
    }

    private fun afterTitleScreenRender(
        screen: Screen,
        drawContext: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        tickDelta: Float
    ) {
        for (layer in layers) {
            drawSnowfallLayer(screen, drawContext, layer)
        }
    }

    private fun drawSnowfallLayer(
        screen: Screen,
        drawContext: GuiGraphics,
        layer: SnowfallLayer
    ) {
        val width = screen.width
        val height = screen.height

        val time = (Util.getMillis() - screenStartTime) / 100.0

        val x = (time * layer.velocityX) % width
        val y = (time + layer.deltaY * height) % height

        val matrices: PoseStack = drawContext.pose()

        matrices.pushPose()
        matrices.translate(x, y, -1.0)

        drawContext.blit(
            SNOW_TEXTURE,
            0, 0,
            0f, 0f,
            width, height,
            64, 256
        )
        drawContext.blit(
            SNOW_TEXTURE,
            0, -height,
            0f, 0f,
            width, height,
            64, 256
        )
        drawContext.blit(
            SNOW_TEXTURE,
            -width, 0,
            0f, 0f,
            width, height,
            64, 256
        )
        drawContext.blit(
            SNOW_TEXTURE,
            -width, -height,
            0f, 0f,
            width, height,
            64, 256
        )

        matrices.popPose()
    }

    private fun generateSnowfallLayers() {
        for (i in layers.indices) {
            layers[i] = SnowfallLayer(
                velocityX = random.nextDouble() * 2.0 - 1.0,
                deltaY = random.nextDouble()
            )
        }
    }

    private fun isHolidaySeason(): Boolean {
        val month = LocalDate.now().monthValue
        return month == 12 || month == 1
    }

    private val SNOW_TEXTURE =
        ResourceLocation.withDefaultNamespace("textures/environment/snow.png")

    private val layers = Array(3) {
        SnowfallLayer(0.0, 0.0)
    }

    private val random: Random = Random

    private var screenStartTime: Long = 0
}

data class SnowfallLayer(
    val velocityX: Double,
    val deltaY: Double
)
