package ru.mail.polis.Command;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import com.sun.net.httpserver.HttpExchange;

import ru.mail.polis.KVDao;


public class DeleteCommand extends KVServiceCommand {
    public DeleteCommand(byte[] id) {
        super(id);
    }

    @Override
    public void execute(@NotNull KVDao dao, @NotNull HttpExchange httpExchange) throws IOException {
        dao.remove(id);
        httpExchange.sendResponseHeaders(202, -1);
    }
}
