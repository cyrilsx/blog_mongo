package org.nexu.bloging;


import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

import java.util.Objects;

import static java.util.Objects.nonNull;

/**
 * Created by cyril on 06/07/16.
 */
public class MongoTestable {


    public static MongoContext beforeEach() throws Exception {
        System.setProperty("export LC_ALL", "C");
        MongodStarter starter = MongodStarter.getDefaultInstance();

        int port = 27017;
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.DEVELOPMENT)
                .net(new Net(port, Network.localhostIsIPv6()))
                .build();

        MongodExecutable mongodExecutable = starter.prepare(mongodConfig);
        MongodProcess mongod = mongodExecutable.start();

         return new MongoContext(mongodExecutable, mongod);
    }

    public static  void afterEach(MongoContext mongoContext) throws Exception {
        if (nonNull(mongoContext) && nonNull(mongoContext.getMongod())) {
            mongoContext.getMongod().stop();
            mongoContext.getMongodExecutable().stop();
        }
    }

}
