package local.database.utils.converters

import androidx.room.TypeConverter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

private open class BaseListConverter<T>(private val serializer: KSerializer<List<T>>) {
    @TypeConverter
    fun fromList(value: List<T>): String {
        return Json.encodeToString(serializer, value)
    }

    @TypeConverter
    fun toList(value: String): List<T> {
        return Json.decodeFromString(serializer, value)
    }
}

class ListStringConverter {
    private val stringListSerializer = serializer<List<String>>()

    @TypeConverter
    fun fromStringList(value: List<String>) = Json.encodeToString(stringListSerializer, value)

    @TypeConverter
    fun toStringList(value: String) = Json.decodeFromString(stringListSerializer, value)

}