package webserver.http.headers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static webserver.Constants.CRLF;

public class Headers<H extends SpecificHeader> {
  private Map<GeneralHeader, List<String>> generalHeaders = new HashMap<>();
  private Map<H, List<String>> specificHeaders = new HashMap<>();
  private Function<H, List<String>> lazyHList = (g) -> new ArrayList<>();
  private Function<GeneralHeader, List<String>> lazyGList = (h) -> new ArrayList<>();


  public void add(GeneralHeader header, String value) {
    generalHeaders.computeIfAbsent(header, lazyGList).add(value);
  }

  public void add(H header, String value) {
    specificHeaders.computeIfAbsent(header, lazyHList).add(value);
  }

  public List<String> remove(GeneralHeader header) {
    return generalHeaders.remove(header);
  }

  public List<String> remove(H header) {
    return specificHeaders.remove(header);
  }

  public List<String> get(H header) {
    return specificHeaders.get(header);
  }

  public List<String> get(GeneralHeader header) {
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
        .append(String.join(",", entry.getValue()))
        .append(CRLF);

    for (var entry : specificHeaders.entrySet())
      builder
        .append(entry.getKey())
        .append(':')
        .append(String.join(",", entry.getValue()))
        .append(CRLF);

    return builder.toString();
  }
}
