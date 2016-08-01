package org.nexu.bloging.domain;

import java.io.Serializable;

/**
 * Created by cyril on 28/04/16.
 */
public class Content implements Serializable {

    private static final long serialVersionUID = 5929348875039214719L;

    private final String payload;
    private final ContentType type;

    public Content(String payload, ContentType type) {
        this.payload = payload;
        this.type = type;
    }

    public ContentType getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }
}
