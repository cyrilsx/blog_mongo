package org.nexu.bloging.controller;

import com.mongodb.reactivestreams.client.MongoDatabase;
import org.nexu.bloging.domain.Post;
import org.nexu.bloging.persistence.CRUDRepository;
import org.nexu.bloging.rest.ListContainer;
import org.nexu.bloging.rest.ResultContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * Created by cyril on 25/04/16.
 */
@RestController
public class PostController {

    @Autowired
    private MongoDatabase mongoDatabase;

    private CRUDRepository<Long, Post> crudRepository;

    @PostConstruct
    private void init() {
        crudRepository = new CRUDRepository<>(mongoDatabase, "Post");
    }

    @RequestMapping(method = RequestMethod.GET)
    public DeferredResult<ResultContainer<Post>> getById(@RequestParam(value="id") Long id) {
        return from(crudRepository.getIdBy(id, Post.class));
    }

    @RequestMapping(method = RequestMethod.GET)
    public DeferredResult<ListContainer<Post>> findAll(@RequestParam(value = "page") int page, @RequestParam(value = "nbElem") int nbElem) {
        return from(crudRepository.findAll(page, nbElem, Post.class));
    }

    @RequestMapping(method = RequestMethod.POST)
    public DeferredResult<ResultContainer<Post>> create(Post post) {
        return from(crudRepository.saveOrUpdate(post).thenApplyAsync(p -> new ResultContainer<>(p, null)));
    }


    @RequestMapping(method = RequestMethod.PUT)
    public DeferredResult<ResultContainer<Post>> update(Post post) {
        return from(crudRepository.saveOrUpdate(post).thenApplyAsync(p -> new ResultContainer<>(p, null)));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public DeferredResult<ResultContainer<Post>> delete(@RequestParam(value = "id") Long id) {
        return from(
                crudRepository.delete(id)
                .thenApplyAsync(p -> new ResultContainer<>(p, null))
        );
    }


    private static <T> DeferredResult<T> from(CompletableFuture<T> completableFuture) {
        final DeferredResult<T> deferred = new DeferredResult<>();
        completableFuture.thenAccept(deferred::setResult);
        completableFuture.exceptionally(ex -> {
            if (ex instanceof CompletionException) {
                deferred.setErrorResult(ex.getCause());
            } else {
                deferred.setErrorResult(ex);
            }
            return null;
        });
        return deferred;
    }


}
