package ru.mail.polis.Command;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import com.sun.net.httpserver.HttpExchange;

import ru.mail.polis.KVDao;


public class GetCommand extends KVServiceCommand {
    public GetCommand(byte[] id) {
        super(id);
    }

    @Override
    public void execute(@NotNull KVDao dao, @NotNull HttpExchange httpExchange) throws IOException {
        final byte[] getValue = dao.get(id);
        httpExchange.sendResponseHeaders(200, getValue.length);
        httpExchange.getResponseBody().write(getValue);
    }
}