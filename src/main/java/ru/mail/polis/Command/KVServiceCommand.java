package ru.mail.polis.Command;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import com.sun.net.httpserver.HttpExchange;

import ru.mail.polis.KVDao;

public abstract class KVServiceCommand {
    protected String id; // Holds the extracted ID

    public KVServiceCommand(String id) {
        this.id = id;
    }

    public abstract void execute(@NotNull KVDao dao, @NotNull HttpExchange httpExchange) throws IOException;
}

