package webserver.http.bodybuilders;

import webserver.http.headers.GeneralHeader;
import webserver.http.headers.SpecificHeader;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static webserver.http.headers.EntityHeaders.ContentLength;
import static webserver.http.headers.EntityHeaders.ContentType;

public class PlainTextBodyBuilder<H extends SpecificHeader> implements BodyBuilder<H> {
  private byte[] bytes;

  public PlainTextBodyBuilder(String text) {
    this.bytes = text.getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public byte[] getBody() {
    return bytes;
  }

  @Override
  public Map<GeneralHeader, String> generalHeaders() {
    return Map.of(ContentLength, String.valueOf(bytes.length), ContentType, "text/html");
  }

  @Override
  public Map<H, String> specificHeaders() {
    return Map.of();
  }

}
