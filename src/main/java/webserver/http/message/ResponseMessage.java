package webserver.http.message;

import webserver.http.headers.Headers;
import webserver.http.headers.ResponseHeaders;

public class ResponseMessage extends GenericMessage {
  public ResponseMessage(StatusLine startLine, Headers<ResponseHeaders> headers, Body body) {
    super(startLine, headers, body);
  }
}
