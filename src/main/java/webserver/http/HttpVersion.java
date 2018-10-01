package webserver.http;

import java.util.HashMap;
import java.util.Map;

public enum HttpVersion {
  ONE_ONE("HTTP/1.1"),
  ONE_ZERO("HTTP/1.0");

  private static Map<String, HttpVersion> versions = new HashMap<>();
  private final String value;

  static {
    for (var version : values())
      versions.put(version.toString().toLowerCase(), version);
  }

  HttpVersion(String s) {
    this.value = s;
  }

  @Override
  public String toString() {
    return value;
  }

  public static HttpVersion getVersion(String str) {
    return versions.get(str);
  }
}
