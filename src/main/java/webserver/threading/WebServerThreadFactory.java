package webserver.threading;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class WebServerThreadFactory implements ThreadFactory {
  @Override
  public Thread newThread(Runnable r) {
    var thread = Executors.defaultThreadFactory().newThread(r);
    thread.setDaemon(true);
    return thread;
  }
}
