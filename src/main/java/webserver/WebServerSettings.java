package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class WebServerSettings {
  private static WebServerSettings instance = new WebServerSettings();
  private Logger logger = LoggerFactory.getLogger(WebServerSettings.class);
  public final Properties properties = new Properties();


  private WebServerSettings() {
    Path path = Paths.get("web/config/webserver.properties");
    var defaults = new Properties();
    try {
      defaults.load(WebServerSettings.class.getResourceAsStream("/web/webserver.properties"));
      if (!Files.exists(path)) {
        logger.info("webserver.properties missing, place here: " + path.toAbsolutePath());
        logger.info("using default webserver.properties");
      } else {
        logger.info("Using webserver.properties located at: " + path.toAbsolutePath());
        logger.info("Missing properties will be loaded with default values");
        properties.load(Files.newInputStream(path));
      }
    } catch (IOException e) {
      // ignored
      e.printStackTrace();
    }
    for (var entry : defaults.entrySet())
      properties.putIfAbsent(entry.getKey(), entry.getValue());
  }

  public static WebServerSettings instance() {
    return instance;
  }
}
