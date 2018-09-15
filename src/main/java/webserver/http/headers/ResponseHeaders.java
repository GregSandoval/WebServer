package webserver.http.headers;

import java.util.HashMap;
import java.util.Map;

public enum ResponseHeaders implements SpecificHeader {
  AcceptRanges("Accept-Ranges"),
  Age,
  ETag,
  Location,
  ProxyAuthenticate("Proxy-Authenticate"),
  RetryAfter("Retry-After"),
  Server,
  Vary,
  WWWAuthenticate("WWW-Authenticate");
  private final String value;
  private static Map<String, ResponseHeaders> headers = new HashMap<>();

  static {
    for (var header : values())
      headers.put(header.toString().toLowerCase(), header);
  }

  ResponseHeaders() {
    this.value = name();
  }

  ResponseHeaders(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }

  public static ResponseHeaders getHeader(String headerName) {
    return headers.get(headerName);
  }
}
