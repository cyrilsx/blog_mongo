package org.nexu.bloging.persistence;

import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
import org.bson.codecs.configuration.CodecRegistries;
import org.hamcrest.CoreMatchers;
import org.junit.*;
import org.junit.runner.RunWith;
import org.nexu.bloging.Application;
import org.nexu.bloging.MongoContext;
import org.nexu.bloging.MongoTestable;
import org.nexu.bloging.domain.Content;
import org.nexu.bloging.domain.ContentType;
import org.nexu.bloging.domain.Post;
import org.nexu.bloging.domain.User;
import org.nexu.bloging.persistence.Codec.LocalDateTimeCodec;
import org.nexu.bloging.persistence.Codec.PostCodec;
import org.nexu.bloging.persistence.Codec.UserCodec;
import org.nexu.bloging.rest.ListContainer;
import org.nexu.bloging.rest.ResultContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Created by cyril on 22/07/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class CRUDRepositoryTest {

    private static final String TEST_COLLECTION = "TEST";

    private static MongoContext mongoContext;

    private CRUDRepository<Long, Post> testRepository = null;
    private AtomicLong sequence = new AtomicLong();

    @Autowired
    private MongoDatabase mongoDatabase;


    @BeforeClass
    public static void beforeClazz() throws Exception {
        mongoContext = MongoTestable.beforeEach();
    }

    @AfterClass
    public static void afterClazz() throws Exception {
        MongoTestable.afterEach(mongoContext);
    }

    @Before
    public void setUp() throws Exception {
        CompletableFuture<Success> successCompletableFuture = new CompletableFuture<>();
        mongoDatabase.getCollection(TEST_COLLECTION).drop().subscribe(new CRUDRepository.ResultSubscriber<>(successCompletableFuture));
        successCompletableFuture.join();
        testRepository = new CRUDRepository<>(mongoDatabase, TEST_COLLECTION);
    }


    @Test
    public void testFindAll() throws Exception {
        // Given a list of documents
        List<Post> items = Stream.of(
                new Post(sequence.incrementAndGet(), "title1", new User("Cyril", "Collen", null, null, null, "cyril@nexu.org"), new Content("html", ContentType.TEXT), LocalDateTime.now()),
                new Post(sequence.incrementAndGet(), "title2", new User("Cyril", "Collen", null, null, null, "cyril@nexu.org"), new Content("html", ContentType.TEXT), LocalDateTime.now()),
                new Post(sequence.incrementAndGet(), "title3", new User("Cyril", "Collen", null, null, null, "cyril@nexu.org"), new Content("html", ContentType.TEXT), LocalDateTime.now()),
                new Post(sequence.incrementAndGet(), "title4", new User("Cyril", "Collen", null, null, null, "cyril@nexu.org"), new Content("html", ContentType.TEXT), LocalDateTime.now())
        ).collect(Collectors.toList());
        CompletableFuture<Success> successCompletableFuture = new CompletableFuture<>();
        mongoDatabase.getCollection(TEST_COLLECTION, Post.class).insertMany(items).subscribe(new CRUDRepository.ResultSubscriber<>(successCompletableFuture));

        // When
        CompletableFuture<ListContainer<Post>> all = testRepository.findAll(0, 10, Post.class);
        ListContainer<Post> resultPosts = all.get(100, TimeUnit.MILLISECONDS);

        // Then
        assertThat(resultPosts.getPayload().size(), CoreMatchers.equalTo(items.size()));
    }

    @Test
    public void testGetIdBy() throws Exception {
        // Given a list of documents
        CompletableFuture<Success> successCompletableFuture = new CompletableFuture<>();
        long id = sequence.incrementAndGet();
        mongoDatabase.getCollection(TEST_COLLECTION, Post.class).insertOne(
                new Post(id, "title1", new User("Cyril", "Collen", null, null, null, "cyril@nexu.org"), new Content("html", ContentType.TEXT), LocalDateTime.now())
                ).subscribe(new CRUDRepository.ResultSubscriber<>(successCompletableFuture));
        assertThat(successCompletableFuture.get(100, TimeUnit.MILLISECONDS), CoreMatchers.notNullValue());

        // When
        CompletableFuture<ResultContainer<Post>> post = testRepository.getIdBy(id, Post.class);
        ResultContainer<Post> resultPost = post.get(100, TimeUnit.MILLISECONDS);

        // Then
        assertThat(resultPost.getPayload(), CoreMatchers.notNullValue());
    }

    @Test
    public void testSaveOrUpdate() throws Exception {
        long id = sequence.incrementAndGet();
        // When
        CompletableFuture<Post> post = testRepository.saveOrUpdate(
                new Post(id, "title1", new User("Cyril", "Collen", null, null, null, "cyril@nexu.org"), new Content("html", ContentType.TEXT), LocalDateTime.now())
        );
        assertThat(post.get(100, TimeUnit.MILLISECONDS), CoreMatchers.notNullValue());

        // Then
        CompletableFuture<ListContainer<Post>> all = testRepository.findAll(0, 1, Post.class);
        ListContainer<Post> resultPosts = all.get(100, TimeUnit.MILLISECONDS);
        assertThat(resultPosts.getPayload().size(), CoreMatchers.equalTo(1));
    }

    @Test
    public void testDelete() throws Exception {
        // Given
        testFindAll();

        // When
        CompletableFuture<Post> post = testRepository.delete(sequence.get());
        post.join();

        // Then
        CompletableFuture<ListContainer<Post>> all = testRepository.findAll(0, 10, Post.class);
        ListContainer<Post> resultPosts = all.get(100, TimeUnit.MILLISECONDS);
        assertThat(resultPosts.getPayload().size(), CoreMatchers.equalTo(3));




    }
}