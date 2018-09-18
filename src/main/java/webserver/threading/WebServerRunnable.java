package webserver.threading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handlers.FileHandler;

import java.io.IOException;
import java.net.ServerSocket;
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
    logger.debug(String.format("Accepting connections, server info: %s", serverSocket));
    while (true) {
      var socket = serverSocket.accept();
      logger.debug(String.format("Accepted connection from: %s", socket));
      executor.execute(new FileHandler(socket));
    }
  }

}
