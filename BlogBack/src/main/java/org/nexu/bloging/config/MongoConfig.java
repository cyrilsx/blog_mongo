package org.nexu.bloging.config;

import com.mongodb.ServerAddress;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.nexu.bloging.persistence.Codec.CodecFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * Created by cyril on 04/07/16.
 */
@Configuration
public class MongoConfig {
    @Bean
    MongoDatabase mongoDatabase() {
        ClusterSettings clusterSettings = ClusterSettings.builder().hosts(Collections.singletonList(new ServerAddress("localhost"))).description("Local Server").build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .clusterSettings(clusterSettings)
                .codecRegistry(CodecRegistries.fromProviders(
                        new CodecFactory()))
                .build();
        return MongoClients.create(settings)
                .getDatabase("blog");
    }
}
