package com.macuguita.petal_smp.server

import net.fabricmc.loader.api.FabricLoader
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.Properties

data class Config(
    val discordWebhookUrl: String
) {
    companion object {

        private const val FILE_NAME = "petal_smp.properties"

        @Throws(IOException::class)
        fun loadOrCreate(): Config {
            val configDir = FabricLoader.getInstance().configDir
            val path: Path = configDir.resolve(FILE_NAME)

            Files.createDirectories(configDir)

            val properties = Properties()

            if (Files.exists(path)) {
                Files.newBufferedReader(path).use { reader ->
                    properties.load(reader)
                }
            }

            val config = Config(
                discordWebhookUrl = get(properties, "discordWebhookUrl", "")
            )

            Files.newBufferedWriter(path).use { writer ->
                properties.store(writer, "Petal SMP Config")
            }

            return config
        }

        private fun get(
            properties: Properties,
            key: String,
            defaultValue: String
        ): String {
            if (!properties.containsKey(key)) {
                properties.setProperty(key, defaultValue)
            }
            return properties.getProperty(key)
        }
    }
}
