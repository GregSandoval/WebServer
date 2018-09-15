package webserver.http.message;

import webserver.http.HttpVersion;

import java.net.URI;

import static webserver.Constants.CRLF;
import static webserver.Constants.SP;

public class RequestLine extends StartLine {
  public final RequestMethod requestMethod;
  public final URI requestUri;
  public final HttpVersion httpVersion;

  public RequestLine(RequestMethod requestMethod, URI requestUri, HttpVersion httpVersion) {
    this.requestMethod = requestMethod;
    this.requestUri = requestUri;
    this.httpVersion = httpVersion;
  }

  @Override
  public String toString() {
    return requestMethod + SP + requestUri + SP + httpVersion + CRLF;
  }
}
