package com.macuguita.petal_smp.server

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.macuguita.petal_smp.common.PetalSMPTweaks
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.server.level.ServerPlayer
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object ServerEntrypoint : DedicatedServerModInitializer {

    private val HTTP_CLIENT: HttpClient = HttpClient.newHttpClient()
    private val GSON: Gson = Gson()

    override fun onInitializeServer() {
        val config = Config.loadOrCreate()

        if (config.discordWebhookUrl.isBlank()) return

        ServerPlayConnectionEvents.JOIN.register(ServerPlayConnectionEvents.Join { handler, _, _ ->
            announce(
                handler.player,
                handler.player.name.string + " joined the server",
                config.discordWebhookUrl
            )
        })
        ServerPlayConnectionEvents.DISCONNECT.register(ServerPlayConnectionEvents.Disconnect { handler, _ ->
            announce(
                handler.player,
                handler.player.name.string + " left the server",
                config.discordWebhookUrl
            )
        })
    }

    private fun announce(player: ServerPlayer, message: String, webookUrl: String) {
        val webhook = Webhook(
            player.name.string,
            message,
            getAvatarUrl(player),
            Webhook.AllowedMentions.NONE
        )
        val json: String = GSON.toJson(webhook)

        val request: HttpRequest = HttpRequest.newBuilder()
            .uri(URI.create(webookUrl))
            .header("Content-Type", "application/json")
            .method("POST", HttpRequest.BodyPublishers.ofString(json))
            .build()

        HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .exceptionally({ e ->
                PetalSMPTweaks.LOGGER.error("Failed to send join webhook", e)
                null
            })
    }

    private fun getAvatarUrl(player: ServerPlayer): String {
        return "https://mc-heads.net/avatar/${player.stringUUID}/128"
    }

    // https://discord.com/developers/docs/resources/webhook#execute-webhook
    private data class Webhook(
        val username: String,
        val content: String,
        @SerializedName("avatar_url")
        val avatarUrl: String,
        @SerializedName("allowed_mentions")
        val allowedMentions: AllowedMentions
    ) {
        data class AllowedMentions(val parse: MutableList<Any>) {
            companion object {
                val NONE = AllowedMentions(mutableListOf<Any>())
            }
        }
    }
}
