package org.nexu.bloging.domain;

import java.io.Serializable;

/**
 * Created by cyril on 28/04/16.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 2622147535419947056L;

    private final String firstName;
    private final String lastName;
    private final String displayName;

    private final String photoPath;
    private final String password;
    private final String email;

    public User(String firstName, String lastName, String displayName, String photoPath, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.photoPath = photoPath;
        this.password = password;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
