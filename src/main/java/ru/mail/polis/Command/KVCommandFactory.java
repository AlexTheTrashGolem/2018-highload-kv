package ru.mail.polis.Command;


import java.nio.charset.StandardCharsets;

import org.jetbrains.annotations.NotNull;

public class KVCommandFactory {
    public static KVServiceCommand getCommand(String method, String query) {
        byte[] id = extractId(query);

        switch (method) {
            case "GET":
                return new GetCommand(id);
            case "DELETE":
                return new DeleteCommand(id);
            case "PUT":
                return new PutCommand(id);
            default:
                return null;
        }
    }

    private static byte[] extractId(@NotNull final String query) {
        final String PREFIX = "id=";
        if (!query.startsWith(PREFIX)) throw new IllegalArgumentException();
        final String id = query.substring(PREFIX.length());
        if (id.isEmpty()) throw new IllegalArgumentException();
        return id.getBytes(StandardCharsets.UTF_8);
    }
}