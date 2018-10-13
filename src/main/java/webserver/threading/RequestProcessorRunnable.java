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
import java.util.Optional;
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
      Optional<RequestMessage> request;
      ResponseMessage response;
      final BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
      final BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
      Scanner scanner = new Scanner(bis);
      do {
        request = RequestParser.parse(bis, scanner);
        response = createResponse(request);

        if (request.isPresent())
          printInfoLine(request.get(), response);

        for (var bytes : response.getBytes())
          bos.write(bytes);
        bos.flush();
      } while (response.getHeader(Connection).equals("keep-alive"));
    } catch (IOException e) {
      logger.error("Could not process request, reason: " + e.getMessage());
    }
  }

  private ResponseMessage createResponse(Optional<RequestMessage> request) {
    ResponseMessage response;
    if (!request.isPresent())
      response = new ResponseMessage(StatusCode._400);
    else
      response = processor.apply(request.get());

    if (request.isPresent() && supportsKeepAlive(response))
      response.addHeader(Connection, "keep-alive");
    else
      response.addHeader(Connection, "close");
    return response;
  }

  private boolean supportsKeepAlive(ResponseMessage response) {
    return response.getHeader(Connection) == null // processor already decided this option
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
