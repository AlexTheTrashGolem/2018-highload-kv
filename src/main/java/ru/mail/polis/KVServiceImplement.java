package ru.mail.polis;
import com.sun.net.httpserver.HttpServer;

import ru.mail.polis.Command.KVCommandFactory;
import ru.mail.polis.Command.KVServiceCommand;

import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.NoSuchElementException;

import org.jetbrains.annotations.NotNull;

import com.sun.net.httpserver.HttpExchange;


public class KVServiceImplement implements KVService {

    @NotNull
    private final HttpServer server;
    @NotNull
    private final KVDao dao;

    public KVServiceImplement(int port, @NotNull final KVDao dao) throws IOException{
        this.server = HttpServer.create( new InetSocketAddress(port),0);

        this.server.createContext("/v0/status", new HttpHandler() {
           @Override
           public void handle(HttpExchange httpExchange) throws IOException{
            final String response = "ONLINE";
            httpExchange.sendResponseHeaders(200, response.getBytes("UTF-8").length);
            httpExchange.getResponseBody().write(response.getBytes("UTF-8"));
            httpExchange.close();
           } 
        });

        this.dao = dao;

        this.server.createContext("/v0/entity", new ErrorHandler(new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                String query = httpExchange.getRequestURI().getQuery();

                try {
                    KVServiceCommand command = KVCommandFactory.getCommand(httpExchange.getRequestMethod(), query);

                    if (command != null) {
                        command.execute(dao, httpExchange);
                    } else {
                        httpExchange.sendResponseHeaders(405, -1); // Method Not Allowed
                    }
                } catch (IllegalArgumentException e) {
                    httpExchange.sendResponseHeaders(400, -1); // Bad Request
                }

                httpExchange.close();
            }
         }));

    }
    @Override
    public void start(){
        this.server.start();
    }
    @Override
    public void stop(){
        this.server.stop(0);
    }

    private static class ErrorHandler implements HttpHandler {
        private final HttpHandler delegate;

        private ErrorHandler(HttpHandler delegate){
            this.delegate = delegate;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException{
            try{
                delegate.handle(httpExchange);
            }
            catch(NoSuchElementException e) {
                httpExchange.sendResponseHeaders(404, -1);
                httpExchange.close();
            }
            catch(IllegalArgumentException e) {
                httpExchange.sendResponseHeaders(400, -1);
                httpExchange.close();
            }
        }
        
    }
}