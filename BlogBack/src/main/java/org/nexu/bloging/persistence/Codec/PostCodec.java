package org.nexu.bloging.persistence.Codec;

import org.bson.*;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistry;
import org.nexu.bloging.domain.Content;
import org.nexu.bloging.domain.Post;
import org.nexu.bloging.domain.User;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by cyril on 22/07/16.
 */
public class PostCodec implements CollectibleCodec<Post> {

    private final CodecRegistry codecRegistry;

    public PostCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public Post generateIdIfAbsentFromDocument(Post document) {
        return document;
    }

    @Override
    public boolean documentHasId(Post document) {
        return Objects.nonNull(document.getId());
    }

    @Override
    public BsonValue getDocumentId(Post document) {
        return new BsonInt64(document.getId());
    }

    @Override
    public Post decode(BsonReader reader, DecoderContext decoderContext) {
        Codec<LocalDateTime> dateCodec = codecRegistry.get(LocalDateTime.class);
        Codec<User> userCodec = codecRegistry.get(User.class);
        Codec<Content> contentCodec = codecRegistry.get(Content.class);

        reader.readStartDocument();
        reader.readName();
        Long id = reader.readInt64();
        reader.readName();
        String title = reader.readString();
        reader.readName();
        LocalDateTime publishingDate = dateCodec.decode(reader, decoderContext);
        reader.readName();
        User user = userCodec.decode(reader, decoderContext);
        reader.readName();
        Content content = contentCodec.decode(reader, decoderContext);


        reader.readEndDocument();
        return new Post(id, title, user, content, publishingDate);
    }

    @Override
    public void encode(BsonWriter writer, Post value, EncoderContext encoderContext) {
        Codec<LocalDateTime> dateCodec = codecRegistry.get(LocalDateTime.class);
        Codec<User> userCodec = codecRegistry.get(User.class);
        Codec<Content> contentCodec = codecRegistry.get(Content.class);

        writer.writeStartDocument();
        writer.writeName("_id");
        writer.writeInt64(value.getId());
        writer.writeName("title");
        writer.writeString(value.getTitle());
        writer.writeName("publishingDate");
        encoderContext.encodeWithChildContext(dateCodec, writer, value.getPublishingDate());
        writer.writeName("user");
        encoderContext.encodeWithChildContext(userCodec, writer, value.getUser());
        writer.writeName("content");
        encoderContext.encodeWithChildContext(contentCodec, writer, value.getContent());
        writer.writeEndDocument();
    }

    @Override
    public Class<Post> getEncoderClass() {
        return Post.class;
    }
}
