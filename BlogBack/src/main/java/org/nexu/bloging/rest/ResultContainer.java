package org.nexu.bloging.rest;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by cyril on 25/04/16.
 */
public class ResultContainer<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 6307692053401555766L;

    private final T payload;
    private final PageInfo page;

    public ResultContainer(T payload, PageInfo page) {
        this.payload = payload;
        this.page = page;
    }

    public T getPayload() {
        return payload;
    }

    public PageInfo getPage() {
        return page;
    }
}
