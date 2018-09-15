package webserver.http.headers;

import java.util.HashMap;
import java.util.Map;

public enum RequestHeaders implements SpecificHeader {
  Accept,
  AcceptCharset("Accept-Charset"),
  AcceptEncoding("Accept-Encoding"),
  AcceptLanguage("Accept-Language"),
  Authorization,
  Expect,
  From,
  Host,
  IfMatch("If-Match"),
  IfModifiedSince("If-Modified-Since"),
  IfNonMatch("If-None-Match"),
  IfRange("If-Range  "),
  IfUnmodifiedSince("If-Unmodified-Since  "),
  MaxForwards("Max-Forwards"),
  ProxyAuthorization("Proxy-Authorization"),
  Range,
  Referer,
  TE,
  UserAgent("User-Agent");

  private final String value;
  private static Map<String, RequestHeaders> headers = new HashMap<>();

  static {
    for (var header : values())
      headers.put(header.toString().toLowerCase(), header);
  }

  RequestHeaders() {
    this.value = name();
  }

  RequestHeaders(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }

  public static RequestHeaders getHeader(String headerName) {
    return headers.get(headerName);
  }
}
