package com.github.cfogrady.mongopatch.spring;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.UUID;

public class MongoPatcherTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static MongoTemplate template;

    private static User user;

    @BeforeAll
    public static void setup() {
        user = new User(UUID.randomUUID(), "Test Person", "test@test.com");
    }

    @AfterEach
    public void clearUsers() {
        template.dropCollection(User.class);
    }
    
    @Test
    public void testPatch() {
    	//MongoPatcher patcher = new MongoPatcher();
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
