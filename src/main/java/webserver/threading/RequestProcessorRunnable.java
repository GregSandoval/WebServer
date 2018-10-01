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

        if (supportsKeepAlive(request, response))
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

  private boolean supportsKeepAlive(RequestMessage request, ResponseMessage response) {
    return response.getHeader(Connection) == null // processor already decided this option
      && request != null // bad requests = auto close connection
      && response.getHttpVersion() == HttpVersion.ONE_ONE // Http 1.1 prefers keep-alive
      && !response.getStatusCode().isClientError() // close after all error responses.
      && !response.getStatusCode().isServerError();
  }

  private void printInfoLine(RequestMessage request, ResponseMessage response) {
    logger.info(
      response.getStatusCode().toCode() + " " + response.getReasonPhrase() + " " +
        String.valueOf(request.getMethod()) +
        ' ' +
        request.getPath() +
        ' ' +
        request.headers().toString().replaceAll("[\r\n]", " ")
    );
  }
}
