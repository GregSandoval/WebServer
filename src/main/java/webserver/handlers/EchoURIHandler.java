package webserver.handlers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.RequestParser;
import webserver.http.message.ResponseMessage;
import webserver.http.message.StatusCode;
import webserver.threading.WebConnectionRunnable;

import java.net.Socket;

import static webserver.http.headers.EntityHeaders.ContentType;

public class EchoURIHandler extends WebConnectionRunnable {
  private final Socket socket;
  private static final Logger logger = LoggerFactory.getLogger(EchoURIHandler.class);

  public EchoURIHandler(Socket socket) {
    super(socket);
    this.socket = socket;
  }

  @Override
  public void main() {
    try (socket) {
      var request = RequestParser.parse(socket.getInputStream());
      logger.debug(String.valueOf("\n" + request));
      var response = new ResponseMessage(StatusCode._200)
        .addBody(request.getStartLine().path.toString())
        .addHeader(ContentType, "text/html");
      logger.debug("Sending response:\n" + response.toString());

      for (byte[] bytes : response.getBytes())
        socket.getOutputStream().write(bytes);

    } catch (Exception e) {
      logger.debug("Error processing Http Message, ignoring:" + socket);
      e.printStackTrace();
    }
  }
}
