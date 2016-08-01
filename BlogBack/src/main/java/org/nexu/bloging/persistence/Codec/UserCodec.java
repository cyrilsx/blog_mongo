package org.nexu.bloging.persistence.Codec;

import org.bson.*;
import org.bson.codecs.*;
import org.nexu.bloging.domain.User;

import java.util.Objects;

/**
 * Created by cyril on 22/07/16.
 */
public class UserCodec implements CollectibleCodec<User> {

    private final Codec<Document> documentCodec = new DocumentCodec();


    @Override
    public User generateIdIfAbsentFromDocument(User document) {
        return document;
    }

    @Override
    public boolean documentHasId(User document) {
        return Objects.nonNull(document.getEmail());
    }

    @Override
    public BsonValue getDocumentId(User document) {
        return new BsonString(document.getEmail());
    }

    @Override
    public User decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader,
                decoderContext);
        String email = (String) document.get("email");
        String firstName = (String) document.get("firstName");
        String lastName = (String) document.get("lastName");
        String displayName = (String) document.get("displayName");
        String password = (String) document.get("password");
        String photopath = (String) document.get("photoPath");
        return new User(firstName, lastName, displayName, photopath, password, email);
    }

    @Override
    public void encode(BsonWriter writer, User value, EncoderContext encoderContext) {
        Document document = new Document();

        if (Objects.nonNull(value.getEmail())) {
            document.put("email", value.getEmail());
        }
        if (Objects.nonNull(value.getDisplayName())) {
            document.put("display", value.getDisplayName());
        }
        if (Objects.nonNull(value.getFirstName())) {
            document.put("firstName", value.getFirstName());
        }
        if (Objects.nonNull(value.getLastName())) {
            document.put("lastName", value.getLastName());
        }
        if (Objects.nonNull(value.getPassword())) {
            document.put("password", value.getPassword());
        }
        if (Objects.nonNull(value.getPhotoPath())) {
            document.put("photoPath", value.getPhotoPath());
        }
        documentCodec.encode(writer, document, encoderContext);
    }

    @Override
    public Class<User> getEncoderClass() {
        return User.class;
    }
}
