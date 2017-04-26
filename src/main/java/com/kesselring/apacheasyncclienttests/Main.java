package com.kesselring.apacheasyncclienttests;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final ExecutorService s = Executors.newFixedThreadPool(1);

    public static void main(String[] args) throws Exception {
        // some slf4j configs
        System.setProperty("org.slf4j.simpleLogger.showDateTime", "true");
        System.setProperty("org.slf4j.simpleLogger.dateTimeFormat", "yyyy-MM-dd'T'HH:mm:ss");
        final Logger log = LoggerFactory.getLogger(Main.class);

        // starting jetty in executor service
        s.submit(new LocalServer());
        Thread.sleep(1000L);
        log.info("startup done");

        // define threadpool for io dispatcher (pool running the callbacks)
        // -> default is 1 per CPU core, but this can be customized
        IOReactorConfig ioConfig = IOReactorConfig.custom().setIoThreadCount(4).build();
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                // set max parallel non-blocking connections -> default is 2
                .setMaxConnPerRoute(8)
                .setDefaultIOReactorConfig(ioConfig).build();
        // Start the client
        httpclient.start();
        log.info("client started");

        // One most likely would want to use a callback for operation result
        final HttpGet request2 = new HttpGet("http://localhost:8080");

        // trigger 10 requests
        httpclient.execute(request2, new LoggingCallback());
        httpclient.execute(request2, new LoggingCallback());
        httpclient.execute(request2, new LoggingCallback());
        httpclient.execute(request2, new LoggingCallback());
        httpclient.execute(request2, new LoggingCallback());
        httpclient.execute(request2, new LoggingCallback());
        httpclient.execute(request2, new LoggingCallback());
        httpclient.execute(request2, new LoggingCallback());
        httpclient.execute(request2, new LoggingCallback());
        httpclient.execute(request2, new LoggingCallback());

        log.info("10 requests triggered, now sleeping 1s");

        Thread.sleep(1000L);

        log.info("sleep done, but requests still working (sleep 10000s inside jettys handler)");

        // additional waiting
        Thread.sleep(60000L);

        log.info("everything should be done by now, shutting down");
        Runtime.getRuntime().exit(0);
    }

    public static class LocalServer implements Runnable {
        final static Logger log = LoggerFactory.getLogger(LocalServer.class);

        public void run() {
            final AtomicInteger count = new AtomicInteger();
            Server server = new Server(8080);

            server.setHandler(new AbstractHandler() {
                public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
                    Integer currentCount = count.addAndGet(1);
                    log.info("request: " + currentCount);
                    httpServletResponse.setContentType("text/plain; charset=utf-8");
                    httpServletResponse.setStatus(HttpServletResponse.SC_OK);

                    PrintWriter out = httpServletResponse.getWriter();
                    try {
                        Thread.sleep(10000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (httpServletResponse != null) {
                        out.print(currentCount + " has been processed successfully");
                    }

                    log.info("done: " + currentCount);
                    request.setHandled(true);
                }
            });
            try {
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            server.dumpStdErr();
            try {
                server.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class LoggingCallback implements FutureCallback<HttpResponse> {
        static final Logger callbackLog = LoggerFactory.getLogger("Callback");

        public void completed(final HttpResponse response2) {
            String body = null;
            try {
                body = EntityUtils.toString(response2.getEntity());
                callbackLog.info("callback pre io: " + "->" + response2.getStatusLine() + ": " + body);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {

            }
            callbackLog.info("callback after io: " + body);
        }

        public void failed(final Exception ex) {
            callbackLog.info("callback: " + "->" + ex);
        }

        public void cancelled() {
            callbackLog.info("callback: " + " cancelled");
        }
    }
}
