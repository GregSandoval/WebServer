package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handlers.FileHandler;
import webserver.threading.RequestProcessorRunnable;
import webserver.threading.WebServerThreadFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class WebServer {
  private final Logger logger = LoggerFactory.getLogger(WebServer.class);
  private final ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newCachedThreadPool(new WebServerThreadFactory());
  private final ServerSocket server;

  public WebServer() throws IOException {
    this(8080);
  }

  public WebServer(int port) throws IOException {
    this.server = new ServerSocket(port);
    pool.execute(new WebServerRunnable());
  }

  private class WebServerRunnable implements Runnable {

    @Override
    public void run() {
      logger.info("Accepting connections on: " + getHttpUrl());
      logger.info("Serving files located in directory: " + Paths.get((String) WebServerSettings.instance().properties.get("dir")).normalize().toAbsolutePath());
      while (true) {
        try {
          var socket = server.accept();
          pool.execute(new RequestProcessorRunnable(socket, new FileHandler()));
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