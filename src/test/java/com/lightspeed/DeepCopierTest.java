package com.lightspeed;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeepCopierTest {

    private DeepCopier copier;

    @BeforeEach
    void setUp() {
        copier = new DeepCopier();
    }

    @Test
    void testManClass() {
        var books = List.of(
                "Pride and Prejudice ",
                "To Kill a Mockingbird",
                "The Great Gatsby");

        var expected = new Man("Alex", 40, books);
        var actual = (Man) copier.createCopy(expected);

        assertNotSame(expected, actual);
        assertEquals(expected, actual);
    }
}
