package webserver.http.message;

import webserver.http.headers.Headers;

import static webserver.Constants.CRLF;

public class GenericMessage {
  public final StartLine startLine;
  public final Headers headers;
  public final Body body;
  private final String string;

  public GenericMessage(StartLine startLine, Headers headers, Body body) {
    this.startLine = startLine;
    this.headers = headers;
    this.body = body;
    this.string = startLine.toString() + headers + CRLF + body;
  }

  @Override
  public String toString() {
    return string;
  }
}
