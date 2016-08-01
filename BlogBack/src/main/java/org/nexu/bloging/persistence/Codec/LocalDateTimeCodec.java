package org.nexu.bloging.persistence.Codec;

import org.bson.*;
import org.bson.codecs.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Created by cyril on 23/07/16.
 */
public class LocalDateTimeCodec implements CollectibleCodec<LocalDateTime> {

    @Override
    public LocalDateTime generateIdIfAbsentFromDocument(LocalDateTime document) {
        return document;
    }

    @Override
    public boolean documentHasId(LocalDateTime document) {
        return false;
    }

    @Override
    public BsonValue getDocumentId(LocalDateTime document) {
        return new BsonDateTime(document.toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    @Override
    public LocalDateTime decode(BsonReader reader, DecoderContext decoderContext) {
        return Instant.ofEpochMilli(reader.readDateTime()).atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    @Override
    public void encode(BsonWriter writer, LocalDateTime value, EncoderContext encoderContext) {
            writer.writeDateTime(value.toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    @Override
    public Class<LocalDateTime> getEncoderClass() {
        return LocalDateTime.class;
    }
}
