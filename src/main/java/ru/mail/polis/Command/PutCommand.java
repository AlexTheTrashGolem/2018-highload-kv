package ru.mail.polis.Command;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import com.sun.net.httpserver.HttpExchange;

import ru.mail.polis.KVDao;


public class PutCommand extends KVServiceCommand {
    public PutCommand(String id) {
        super(id);
    }

    @Override
    public void execute(@NotNull KVDao dao, @NotNull HttpExchange httpExchange) throws IOException {
        final int contentLength = Integer.parseInt(httpExchange.getRequestHeaders().getFirst("Content-Length"));
        final byte[] putValue = new byte[contentLength];

        if (httpExchange.getRequestBody().read(putValue) != putValue.length) {
            throw new IOException("Failed to read request body");
        }

        dao.upsert(id, putValue);
        httpExchange.sendResponseHeaders(201, -1);
    }
}