package webserver.threading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class WebConnectionRunnable implements Runnable {
  private final List<Closeable> closeables = new ArrayList<>();
  private final Logger logger = LoggerFactory.getLogger(WebConnectionRunnable.class);

  public WebConnectionRunnable(Closeable... closeables) {
    this.closeables.addAll(List.of(closeables));
  }

  public abstract void main() throws Exception;

  private void close() {
    for (var closeable : closeables) {
      try {
        closeable.close();
      } catch (IOException e) {
        logger.error("Problem closing : " + closeable + "; Error: " + e.getMessage());
      }
    }
  }

  @Override
  public void run() {
    try {
      main();
      close();
    } catch (Exception e) {
      logger.error("Problem processing Http Message: " + e.getMessage());
    }
  }
}
