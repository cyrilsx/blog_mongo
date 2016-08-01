package org.nexu.bloging;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;

/**
 * Created by cyril on 22/07/16.
 */
public final class MongoContext {
    private final MongodExecutable mongodExecutable;
    private final MongodProcess mongod;

    public MongoContext(MongodExecutable mongodExecutable, MongodProcess mongod) {
        this.mongodExecutable = mongodExecutable;
        this.mongod = mongod;
    }

    public MongodExecutable getMongodExecutable() {
        return mongodExecutable;
    }

    public MongodProcess getMongod() {
        return mongod;
    }
}
