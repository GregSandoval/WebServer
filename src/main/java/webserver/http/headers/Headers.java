package webserver.http.headers;

import java.util.HashMap;
import java.util.Map;

import static webserver.Constants.CRLF;

public class Headers<H extends SpecificHeader> {
  private Map<GeneralHeader, String> generalHeaders = new HashMap<>();
  private Map<H, String> specificHeaders = new HashMap<>();

  public String add(GeneralHeader header, String value) {
    return generalHeaders.put(header, value);
  }

  public String add(H header, String value) {
    return specificHeaders.put(header, value);
  }

  public String remove(GeneralHeader header) {
    return generalHeaders.remove(header);
  }

  public String remove(H header) {
    return specificHeaders.remove(header);
  }

  public String get(H header) {
    return specificHeaders.get(header);
  }

  public String get(GeneralHeader header) {
    return generalHeaders.get(header);
  }

  public boolean contains(H header) {
    return specificHeaders.containsKey(header);
  }

  public boolean contains(GeneralHeader header) {
    return generalHeaders.containsKey(header);
  }

  @Override
  public String toString() {
    var builder = new StringBuilder();

    for (var entry : generalHeaders.entrySet())
      builder
        .append(entry.getKey())
        .append(':')
        .append(entry.getValue())
        .append(CRLF);

    for (var entry : specificHeaders.entrySet())
      builder
        .append(entry.getKey())
        .append(':')
        .append(entry.getValue())
        .append(CRLF);

    return builder.toString();
  }
}
