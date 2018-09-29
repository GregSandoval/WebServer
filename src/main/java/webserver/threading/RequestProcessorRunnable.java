package webserver.threading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.RequestParser;
import webserver.http.message.RequestMessage;
import webserver.http.message.ResponseMessage;
import webserver.http.message.StatusCode;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Function;

public class RequestProcessorRunnable implements Runnable {
  private final Logger logger = LoggerFactory.getLogger(RequestProcessorRunnable.class);
  private final Function<RequestMessage, ResponseMessage> processor;
  private final Socket socket;

  public RequestProcessorRunnable(Socket socket, Function<RequestMessage, ResponseMessage> processor) {
    this.processor = processor;
    this.socket = socket;
  }

  @Override
  public void run() {
    try (socket) {
      final var request = RequestParser.parse(socket.getInputStream());
      final ResponseMessage response;

      if (request == null) // bad request
        response = new ResponseMessage(StatusCode._400);
      else
        response = processor.apply(request);

      if (request != null)
        printInfoLine(request, response);

      final var os = new BufferedOutputStream(socket.getOutputStream());
      for (var bytes : response.getBytes())
        os.write(bytes);
      os.flush();
    } catch (IOException e) {
      logger.error("Could not process request, reason: " + e.getMessage());
    }
  }

  private void printInfoLine(RequestMessage request, ResponseMessage response) {
    logger.info(
      response.getStartLine().statusCode + " " + response.getStartLine().reasonPhrase + " " +
        String.valueOf(request.getStartLine().requestMethod) +
        ' ' +
        request.getStartLine().path +
        ' ' +
        request.headers().toString().replaceAll("[\r\n]", " ")
    );
  }
}
