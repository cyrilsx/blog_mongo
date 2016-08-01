package org.nexu.bloging.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by cyril on 25/04/16.
 */
public class Post implements Serializable, Identifiable<Long> {

    private static final long serialVersionUID = -209125418084522654L;

    private final Long id;
    private final String title;
    private final User user;
    private final Content content;
    private final LocalDateTime publishingDate;


    public Post(Long id, String title, User user, Content content, LocalDateTime publishingDate) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.content = content;
        this.publishingDate = publishingDate;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getUser() {
        return user;
    }

    public Content getContent() {
        return content;
    }

    public LocalDateTime getPublishingDate() {
        return publishingDate;
    }
}
