package webserver.threading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpVersion;
import webserver.http.RequestParser;
import webserver.http.message.RequestMessage;
import webserver.http.message.ResponseMessage;
import webserver.http.message.StatusCode;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Function;

import static webserver.http.headers.GeneralHeaders.Connection;

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
      RequestMessage request;
      ResponseMessage response;
      final BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
      final BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
      Scanner scanner = new Scanner(bis);
      do {
        request = RequestParser.parse(bis, scanner);

        if (request == null)
          response = new ResponseMessage(StatusCode._400);
        else
          response = processor.apply(request);

        // signal close when not using HTTP 1.1 or if request is bad. Otherwise send keep alive.
        if (response.headers().get(Connection) == null && request != null && request.getStartLine().httpVersion == HttpVersion.ONE_ONE)
          response.addHeader(Connection, "keep-alive");
        else
          response.addHeader(Connection, "close");

        if (request != null)
          printInfoLine(request, response);

        for (var bytes : response.getBytes())
          bos.write(bytes);
        bos.flush();
      } while (response.headers().get(Connection).equals("keep-alive"));
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
