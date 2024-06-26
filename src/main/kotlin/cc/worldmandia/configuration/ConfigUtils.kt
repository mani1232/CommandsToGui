package cc.worldmandia.configuration

import cc.worldmandia.configuration.data.Config
import cc.worldmandia.configuration.data.DataSave
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.charleskorn.kaml.decodeFromStream
import com.charleskorn.kaml.encodeToStream
import java.io.File

object ConfigUtils {
    lateinit var dataSave: DataSave
    lateinit var config: Config

    val yaml = Yaml(
        configuration = YamlConfiguration(
            encodeDefaults = true,
        )
    )

    inline fun <reified T> load(file: File, defaultValue: T): T {
        return if (file.exists()) {
            yaml.decodeFromStream(file.inputStream())
        } else {
            yaml.encodeToStream(defaultValue, file.outputStream())
            defaultValue
        }
    }

    inline fun <reified T> update(file: File, data: T) {
        return yaml.encodeToStream(data, file.outputStream())
    }


}