package webserver.http.headers;

import java.util.HashMap;
import java.util.Map;

public enum EntityHeaders implements GeneralHeader {
  Allow,
  ContentEncoding("Content-Encoding"),
  ContentLanguage("Content-Language"),
  ContentLength("Content-Length"),
  ContentLocation("Content-Location"),
  ContentMD5("Content-MD5"),
  ContentRange("Content-Range"),
  ContentType("Content-Type"),
  Expires,
  LastModified("Last-Modified");

  private final String caseSensitiveValue;
  private static Map<String, EntityHeaders> headers = new HashMap<>();

  static {
    for (EntityHeaders header : values())
      headers.put(header.caseSensitiveValue.toLowerCase(), header);
  }

  EntityHeaders() {
    this.caseSensitiveValue = name();
  }

  EntityHeaders(String value) {
    this.caseSensitiveValue = value;
  }

  @Override
  public String toString() {
    return caseSensitiveValue;
  }

  public static EntityHeaders getHeader(String headerName) {
    return headers.get(headerName);
  }
}
