package webserver.threading;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class WebConnectionRunnable implements Runnable {
  private final List<Closeable> closeables = new ArrayList<>();

  public WebConnectionRunnable(Closeable... closeables) {
    this.closeables.addAll(List.of(closeables));
  }

  public abstract void main() throws IOException, Exception;

  private void close() {
    for (var closeable : closeables) {
      try {
        closeable.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    System.out.println("Closed " + this);
  }

  @Override
  public void run() {
    try {
      main();
      close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
