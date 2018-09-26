package webserver.threading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.WebServerSettings;
import webserver.handlers.FileHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;

public class WebServerRunnable extends WebConnectionRunnable {
  private final Logger logger = LoggerFactory.getLogger(WebServerRunnable.class);
  private final ServerSocket serverSocket;
  private final ExecutorService executor;

  public WebServerRunnable(ExecutorService executor, ServerSocket serverSocket) {
    super(serverSocket);
    this.serverSocket = serverSocket;
    this.executor = executor;
  }

  @Override
  public void main() throws IOException {
    logger.info("Accepting connections on: " + getHttpUrl());
    logger.info("Serving files located in directory: " + Paths.get((String) WebServerSettings.instance().properties.get("dir")).normalize().toAbsolutePath());
    while (true) {
      var socket = serverSocket.accept();
      executor.execute(new FileHandler(socket));
    }
  }

  private String getHttpUrl() {
    return "http://localhost:" + serverSocket.getLocalPort() + "/";
  }

}
