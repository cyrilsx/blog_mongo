package org.nexu.bloging.persistence;

import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
import org.bson.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.nexu.bloging.controller.PostController;
import org.nexu.bloging.domain.Identifiable;
import org.nexu.bloging.persistence.Codec.LocalDateTimeCodec;
import org.nexu.bloging.persistence.Codec.PostCodec;
import org.nexu.bloging.persistence.Codec.UserCodec;
import org.nexu.bloging.rest.ListContainer;
import org.nexu.bloging.rest.PageInfo;
import org.nexu.bloging.rest.ResultContainer;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Created by cyril on 10/07/16.
 */
public final class CRUDRepository<K extends Serializable, V extends Identifiable<K>> {

    private final MongoDatabase mongoDatabase;
    private final String collectionName;

    public CRUDRepository(MongoDatabase mongoDatabase, String collectionName) {
        this.mongoDatabase = mongoDatabase;
        this.collectionName = collectionName;
    }


    public CompletableFuture<ListContainer<V>> findAll(int page, int nbElem, Class<V> clazz) {
        Objects.requireNonNull(clazz, "clazz is mandatory to search object");

        CompletableFuture<ListContainer<V>> resultContainerDeferredResult = new CompletableFuture<>();
        mongoDatabase.getCollection(collectionName)
                .find(clazz)
                .skip(page * nbElem)
                .limit(nbElem)
                .subscribe(new ListPostSubscriber<>(resultContainerDeferredResult));
        return resultContainerDeferredResult;
    }

    public CompletableFuture<ResultContainer<V>>  getIdBy(K key, Class<V> clazz) {
        Objects.requireNonNull(key, "key is mandatory to search object");
        Objects.requireNonNull(clazz, "clazz is mandatory to search object");

        CompletableFuture<V> resultContainerCompletableFuture = new CompletableFuture<>();
        mongoDatabase.getCollection(collectionName)
                .find(new BsonDocument("_id", toBsonValue(key, (Class<K>) key.getClass())), clazz)
                .first()
                .subscribe(new ResultSubscriber<>(resultContainerCompletableFuture));
        return resultContainerCompletableFuture
                .thenApply(res -> new ResultContainer<>(res, null));
    }


    private <K> BsonValue toBsonValue(K key, Class<K> clazz) {
        if (clazz.equals(Long.class)) {
            return new BsonInt64((Long) key);
        }
        return new BsonString(key.toString());
    }


    public CompletableFuture<V> saveOrUpdate(V value) {
        Class<V> clazz = (Class<V>) value.getClass();
        return getIdBy(value.getId(), clazz)
                .thenCompose(r -> {
                    if (Objects.nonNull(r)){
                        CompletableFuture<UpdateResult> updateResult = new CompletableFuture<>();
                        mongoDatabase.getCollection(collectionName, clazz)
                                .updateOne(new BsonDocument("_id", toBsonValue(r.getPayload().getId(), (Class<K>) r.getPayload().getId().getClass())), new Document("$set",value))
                                .subscribe(new ResultSubscriber<>(updateResult));
                        return updateResult.thenApply((any) -> value);

                    } else {
                        CompletableFuture<Success> successCompletableFuture = new CompletableFuture<>();
                        mongoDatabase.getCollection(collectionName, clazz)
                                .insertOne(value)
                                .subscribe(new ResultSubscriber<>(successCompletableFuture));
                        return successCompletableFuture.thenApply((any) -> value);
                    }
                });

    }

    public CompletableFuture<V> delete(K key) {
        Class<V> clazz = (Class<V>) key.getClass();
        CompletableFuture<V> completableFuture = new CompletableFuture<>();
        mongoDatabase.getCollection(collectionName, clazz)
                .findOneAndDelete(new BsonDocument("_id", toBsonValue(key, (Class<K>) key.getClass()))).subscribe(new ResultSubscriber<>(completableFuture));
        return completableFuture;
    }


    public static class ResultSubscriber<T> implements Subscriber<T> {
        private static final Logger logger = LoggerFactory.getLogger(PostController.class);


        private final CompletableFuture<T> completableFuture;
        private T value;

        public ResultSubscriber(CompletableFuture<T> completableFuture) {
            this.completableFuture = completableFuture;
        }

        @Override
        public void onSubscribe(Subscription s) {
            s.request(10);
            logger.info("subscription activated");
        }

        @Override
        public void onNext(T success) {
            logger.info("subscription activated");
            this.value = success;
        }

        @Override
        public void onError(Throwable t) {
            logger.info("Error while executing Post operation", t);
            completableFuture.completeExceptionally(t);
        }

        @Override
        public void onComplete() {
            logger.info("request complete, sending response with {}", value);
            completableFuture.complete(value);
        }
    }


    private static class ListPostSubscriber<P extends Serializable> implements Subscriber<P> {

        private static final Logger logger = LoggerFactory.getLogger(PostController.class);

        private final CompletableFuture<ListContainer<P>> resultContainerDeferredResult;
        private final List<P> currentResult = new ArrayList<>();

        private ListPostSubscriber(CompletableFuture<ListContainer<P>> resultContainerDeferredResult) {
            this.resultContainerDeferredResult = resultContainerDeferredResult;
        }

        @Override
        public void onSubscribe(Subscription s) {
            s.request(100L);
            logger.info("subscription activated");
        }

        @Override
        public void onNext(P post) {
            logger.info("get post {}", post);
            currentResult.add(post);
        }

        @Override
        public void onError(Throwable t) {
            logger.info("Error while executing Post operation", t);
            resultContainerDeferredResult.completeExceptionally(t);
        }

        @Override
        public void onComplete() {
            logger.info("request complete, sending response with {} elem", currentResult.size());
            resultContainerDeferredResult.complete(new ListContainer<>(currentResult, new PageInfo()));
        }
    }



}
