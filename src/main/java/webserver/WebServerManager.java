package webserver;

import webserver.threading.WebServerRunnable;
import webserver.threading.WebServerThreadFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServerManager {
  private ExecutorService pool = Executors.newCachedThreadPool(new WebServerThreadFactory());

  public WebServerManager() throws IOException {
    this(8080);
  }

  public WebServerManager(int port) throws IOException {
    pool.execute(new WebServerRunnable(pool, new ServerSocket(port)));
  }

}
