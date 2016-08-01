package org.nexu.bloging.persistence.Codec;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.nexu.bloging.domain.Content;
import org.nexu.bloging.domain.Post;
import org.nexu.bloging.domain.User;

import java.time.LocalDateTime;

/**
 * Created by cyril on 22/07/16.
 */
public class CodecFactory implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> type, CodecRegistry registry) {
        if (type == Content.class) {
            return (Codec<T>) new ContentCodec();
        }
        if (type == LocalDateTime.class) {
            return (Codec<T>) new LocalDateTimeCodec();
        }
        if (type == Post.class) {
            return (Codec<T>) new PostCodec(registry);
        }
        if (type == User.class) {
            return (Codec<T>) new UserCodec();
        }
        return null;
    }
}
