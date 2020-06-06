package com.github.cfogrady.spring.mongopatch;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.exceptions.DistributionException;
import de.flapdoodle.embed.process.runtime.Network;

public class MongoPatcherTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static MongodProcess mongod;
    private static MongodExecutable mongodExecutable;

    private static MongoClient client;
    private static MongoTemplate template;

    private static User user;

    @BeforeAll
    public static void setup() {
        MongodExecutable mongodExecutable = null;
        int port = 27018;
        try {
            MongodStarter starter = MongodStarter.getDefaultInstance();

            String bindIp = "localhost";
            IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(bindIp, port, Network.localhostIsIPv6()))
                .build();

            mongodExecutable = starter.prepare(mongodConfig);
            mongod = mongodExecutable.start();
            client = MongoClients.create("mongodb://localhost:" + port);
            template = new MongoTemplate(client, "test");
        } catch (DistributionException | IOException e) {
            cleanup();
        }

        user = new User(UUID.randomUUID(), "Test Person", "test@test.com");
    }

    @AfterAll
    public static void cleanup() {
        if (mongod != null) {
            mongod.stop();
            mongod = null;
        }
        if (mongodExecutable != null) {
            mongodExecutable.stop();
            mongodExecutable = null;
        }
    }

    @AfterEach
    public void clearUsers() {
        template.dropCollection(User.class);
    }

    @Test
    public void testOperations() {
        logger.info("Verify read and write to embedded mongo works.");
        template.insert(user);
        List<User> users = template.findAll(User.class);
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(user, users.get(0));
    }

    @Document(collection = "user")
    @TypeAlias("User")
    public static class User {
        public final UUID id;
        public final String name;
        public final String email;

        public User(UUID id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        @Override
        public int hashCode() {
            return Objects.hash(email, id, name);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            User other = (User) obj;
            return Objects.equals(email, other.email) && Objects.equals(id, other.id) && Objects.equals(name, other.name);
        }

    }
}
