package dev.nitramnibus.nitrameco.database;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UuidConverter {

    public static byte[] toBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static UUID fromBytes(byte[] array) {
        ByteBuffer bb = ByteBuffer.wrap(array);
        long high = bb.getLong();
        long low = bb.getLong();
        return new UUID (high, low);
    }

}
