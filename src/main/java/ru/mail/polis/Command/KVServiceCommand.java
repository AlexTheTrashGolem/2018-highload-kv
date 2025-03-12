package ru.mail.polis.Command;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import com.sun.net.httpserver.HttpExchange;

import ru.mail.polis.KVDao;

public abstract class KVServiceCommand {
    protected byte[] id; // Holds the extracted ID

    public KVServiceCommand(byte[] id) {
        this.id = id;
    }

    public abstract void execute(@NotNull KVDao dao, @NotNull HttpExchange httpExchange) throws IOException;
}

