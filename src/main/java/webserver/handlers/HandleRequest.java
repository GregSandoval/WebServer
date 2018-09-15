package webserver.handlers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpVersion;
import webserver.http.RequestParser;
import webserver.http.headers.Headers;
import webserver.http.headers.ResponseHeaders;
import webserver.http.message.Body;
import webserver.http.message.ResponseMessage;
import webserver.http.message.StatusCode;
import webserver.http.message.StatusLine;
import webserver.threading.WebConnectionRunnable;

import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static webserver.http.headers.EntityHeaders.ContentLength;
import static webserver.http.headers.EntityHeaders.ContentType;

public class HandleRequest extends WebConnectionRunnable {
  private final Socket socket;
  private static final Logger logger = LoggerFactory.getLogger(HandleRequest.class);

  public HandleRequest(Socket socket) {
    super(socket);
    this.socket = socket;
  }

  @Override
  public void main() {
    try (socket) {
      var request = RequestParser.parse(socket.getInputStream());
      logger.debug(String.valueOf("\n" + request));
      var body = new Body("Hello World!");
      var headers = new Headers<ResponseHeaders>();
      headers.add(ContentLength, String.valueOf(body.entityBody.getBytes(StandardCharsets.UTF_8).length));
      headers.add(ContentType, "application/json");
      var response = new ResponseMessage(
        new StatusLine(HttpVersion.ONE_ONE, StatusCode._200),
        headers,
        body);
      logger.debug("Sending response:\n" + response.toString());
      socket.getOutputStream().write(response.toString().getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      logger.debug("Error processing Http Message, ignoring:" + socket);
      e.printStackTrace();
    }
  }
}
