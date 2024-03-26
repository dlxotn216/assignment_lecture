package io.taesu.lectureapi.common.config.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.deser.std.StringDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.io.IOException

/**
 * Created by itaesu on 2024/03/21.
 *
 * @author Lee Tae Su
 */
class EmptyStringAsNullModule: SimpleModule() {
    init {
        this.addDeserializer(String::class.java, EmptyStringAsNullDeserializer())
        this.addSerializer(String::class.java, EmptyStringAsNullSerializer())
    }
}

open class EmptyStringAsNullDeserializer: StdDeserializer<String>(String::class.java) {
    @Throws(IOException::class)
    override fun deserialize(parser: JsonParser, context: DeserializationContext): String? {
        val result = StringDeserializer.instance.deserialize(parser, context)
        return nullIfBlank(result)
    }

    fun nullIfBlank(value: String?) = value?.trim()?.ifBlank { null }
}

class EmptyStringAsNullSerializer: StdSerializer<String>(String::class.java) {
    @Throws(IOException::class)
    override fun serialize(value: String, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value)
    }
}
