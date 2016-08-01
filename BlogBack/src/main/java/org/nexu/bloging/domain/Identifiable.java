package org.nexu.bloging.domain;

import java.io.Serializable;

/**
 * Created by cyril on 10/07/16.
 */
public interface Identifiable<K extends Serializable> extends Serializable {

    K getId();


}
