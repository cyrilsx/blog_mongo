package org.nexu.bloging.persistence.Codec;

import org.bson.*;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistry;
import org.nexu.bloging.domain.Content;
import org.nexu.bloging.domain.ContentType;
import org.nexu.bloging.domain.Post;
import org.nexu.bloging.domain.User;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by cyril on 22/07/16.
 */
public class ContentCodec implements CollectibleCodec<Content> {

    @Override
    public Content generateIdIfAbsentFromDocument(Content document) {
        return document;
    }

    @Override
    public boolean documentHasId(Content document) {
        return false;
    }

    @Override
    public BsonValue getDocumentId(Content document) {
        return null;
    }

    @Override
    public Content decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        reader.readName();
        String payload = reader.readString();
        reader.readName();
        String type = reader.readString();
        reader.readEndDocument();
        return new Content(payload, ContentType.valueOf(type));
    }

    @Override
    public void encode(BsonWriter writer, Content value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeName("payload");
        writer.writeString(value.getPayload());
        if (Objects.nonNull(value.getType())) {
            writer.writeName("contentType");
            writer.writeString(value.getType().name());
        }
        writer.writeEndDocument();

    }

    @Override
    public Class<Content> getEncoderClass() {
        return Content.class;
    }
}
