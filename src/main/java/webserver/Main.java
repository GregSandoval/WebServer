package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
  private static Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws IOException {
    var serverManager = new WebServerManager();
    var serverSettings = WebServerSettings.instance();
    logger.info("Press Enter key to stop server");
    int ignored = System.in.read();
  }
}
