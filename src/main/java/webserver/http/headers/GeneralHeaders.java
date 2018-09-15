package webserver.http.headers;

import java.util.HashMap;
import java.util.Map;

public enum GeneralHeaders implements GeneralHeader {
  CacheControl("Cache-Control"),
  Connection,
  Date,
  Pragma,
  Trailer,
  TransferEncoding("Transfer-Encoding"),
  Upgrade,
  Via,
  Warning;


  private final String value;
  private static Map<String, GeneralHeader> headers = new HashMap<>();

  static {
    for (GeneralHeader header : values())
      headers.put(header.toString().toLowerCase(), header);
  }

  GeneralHeaders() {
    this.value = name();
  }

  GeneralHeaders(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }

  public static GeneralHeader getHeader(String headerName) {
    return headers.get(headerName);
  }
}
