package dev.nitramnibus.nitrameco.database;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;


public class UuidConverterTest {

    @Test
    void testUuidConvertion() {

        UUID uuid = UUID.randomUUID();
        UUID recovered = UuidConverter.fromBytes(UuidConverter.toBytes(uuid));
        assertEquals(uuid, recovered);
    }

}
