package webserver.handlers;


import webserver.http.message.RequestMessage;
import webserver.http.message.ResponseMessage;
import webserver.http.message.StatusCode;

import java.util.function.Function;

import static webserver.http.headers.EntityHeaders.ContentType;

public class EchoURIHandler implements Function<RequestMessage, ResponseMessage> {

  @Override
  public ResponseMessage apply(RequestMessage request) {
    return new ResponseMessage(StatusCode._200)
      .addBody(request.getPath().toString())
      .addHeader(ContentType, "text/html");
  }
}
