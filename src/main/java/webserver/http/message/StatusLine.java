package webserver.http.message;

import webserver.http.HttpVersion;

import static webserver.Constants.CRLF;
import static webserver.Constants.SP;

public class StatusLine extends StartLine {
  public final HttpVersion httpVersion;
  public final StatusCode statusCode;
  public final String reasonPhrase;

  public StatusLine(StatusCode statusCode) {
    this(HttpVersion.ONE_ONE, statusCode);
  }

  public StatusLine(HttpVersion httpVersion, StatusCode statusCode) {
    this.httpVersion = httpVersion;
    this.statusCode = statusCode;
    this.reasonPhrase = statusCode.description;
  }

  @Override
  public String toString() {
    return httpVersion + SP + statusCode.toCode() + SP + reasonPhrase + CRLF;
  }
}
