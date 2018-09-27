package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.message.RequestMessage;
import webserver.http.message.ResponseMessage;
import webserver.threading.RequestProcessorRunnable;
import webserver.threading.WebServerThreadFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;

public class WebServer {
  private final Logger logger = LoggerFactory.getLogger(WebServer.class);
  private final ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newCachedThreadPool(new WebServerThreadFactory());
  private final ServerSocket server;
  private final Function<RequestMessage, ResponseMessage> processor;

  public WebServer(int port, Function<RequestMessage, ResponseMessage> processor) throws IOException {
    this.server = new ServerSocket(port);
    this.processor = processor;
    pool.execute(new WebServerRunnable());
  }

  private class WebServerRunnable implements Runnable {

    @Override
    public void run() {
      logger.info("Accepting connections on: " + getHttpUrl());
      while (true) {
        try {
          var socket = server.accept();
          pool.execute(new RequestProcessorRunnable(socket, processor));
        } catch (IOException e) {
          logger.error("Unable to accept connection, reason: " + e.getMessage());
        }
      }
    }

    private String getHttpUrl() {
      return "http://localhost:" + server.getLocalPort() + "/";
    }
  }

}