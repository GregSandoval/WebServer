package webserver.http.message;

import webserver.http.headers.Headers;
import webserver.http.headers.RequestHeaders;

public class RequestMessage extends GenericMessage {
  public RequestMessage(RequestLine startLine, Headers<RequestHeaders> headers, Body body) {
    super(startLine, headers, body);
  }
}
