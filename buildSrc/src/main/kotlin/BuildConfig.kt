object BuildConfig {
    val minecraftVersion: String = "1.21.1"
    val minecraftVersionRange: String = ">=1.21.1"
    val supportedVersions: Array<String> = arrayOf("1.21.1")
    val parchmentMappings: String? = null
    val loaderVersion: String = "0.18.2"

    val modVersion: String = "$minecraftVersion-0.0.5"
    val mavenGroup: String = "com.macuguita.petal_smp"
    val modId: String = "petal_tweaks"
    val modName: String = "Petal SMP Tweaks"
    val description: String = "petal smp tweaks"
    val license: String = "MIT"

    val fabricVersion: String = "0.116.7+$minecraftVersion"
    val fabricKotlinVersion: String = "1.13.7+kotlin.2.2.21"
    val modMenuVersion: String = "11.0.3"
    val emiVersion: String = "1.1.22+" + minecraftVersion

    val maculibVersion: String = "1.0.5-1.21.1"
}
