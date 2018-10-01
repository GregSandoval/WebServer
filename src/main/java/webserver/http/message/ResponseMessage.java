package webserver.http.message;

import webserver.http.HttpVersion;
import webserver.http.headers.ResponseHeaders;

public class ResponseMessage extends GenericMessage<ResponseMessage, StatusLine, ResponseHeaders> {

  public ResponseMessage(StatusCode statusCode) {
    super(new StatusLine(HttpVersion.ONE_ONE, statusCode));
  }

  public ResponseMessage(StatusLine statusLine) {
    super(statusLine);
  }

  public HttpVersion getHttpVersion() {
    return getStartLine().httpVersion;
  }

  public StatusCode getStatusCode() {
    return getStartLine().statusCode;
  }

  public String getReasonPhrase() {
    return getStartLine().reasonPhrase;
  }

}
