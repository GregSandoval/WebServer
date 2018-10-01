package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handlers.FileHandler;
import webserver.http.message.ResponseMessage;
import webserver.http.message.StatusCode;

import java.io.IOException;

public class Main {
  private static Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws IOException {
    var echoserver = new WebServer(9090, request -> {
      var response = new ResponseMessage(StatusCode._200);
      response.addBody(
        "Hello world! this is a test \n" +
          "The request Http message is listed below! (body is in byte form)\n\n" +
          request
      );
      return response;
    });

    var fileServer = new WebServer(8080, new FileHandler());

    var restServer = new RestServer(9999)
      .get("/", request -> new ResponseMessage(StatusCode._200).addBody("Nice!"))
      .get("/helloworld", request -> new ResponseMessage(StatusCode._200).addBody("HELLO WORLD"));

    var serverSettings = WebServerSettings.instance();
    logger.info("Press Enter key to stop server");
    int ignored = System.in.read();
  }
}
