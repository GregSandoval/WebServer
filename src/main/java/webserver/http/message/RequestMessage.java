package webserver.http.message;

import webserver.http.headers.RequestHeaders;

public class RequestMessage extends GenericMessage<RequestMessage, RequestLine, RequestHeaders> {
  public RequestMessage(RequestLine startLine) {
    super(startLine);
  }
}
