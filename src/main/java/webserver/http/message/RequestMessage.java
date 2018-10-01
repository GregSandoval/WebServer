package webserver.http.message;

import webserver.http.HttpVersion;
import webserver.http.headers.RequestHeaders;

import java.nio.file.Path;

public class RequestMessage extends GenericMessage<RequestMessage, RequestLine, RequestHeaders> {
  public RequestMessage(RequestLine startLine) {
    super(startLine);
  }

  public RequestMethod getMethod() {
    return getStartLine().requestMethod;
  }

  public Path getPath() {
    return getStartLine().path;
  }

  public HttpVersion getHttpVersion() {
    return getStartLine().httpVersion;
  }
}
