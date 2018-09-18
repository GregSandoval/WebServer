package webserver.http.message;

import webserver.http.HttpVersion;

import java.nio.file.Path;

import static webserver.Constants.CRLF;
import static webserver.Constants.SP;

public class RequestLine extends StartLine {
  public final RequestMethod requestMethod;
  public final Path path;
  public final HttpVersion httpVersion;

  public RequestLine(RequestMethod requestMethod, Path path, HttpVersion httpVersion) {
    this.requestMethod = requestMethod;
    this.path = path;
    this.httpVersion = httpVersion;
  }

  @Override
  public String toString() {
    return requestMethod + SP + path + SP + httpVersion + CRLF;
  }
}
