package webserver.http.message;

import webserver.http.bodybuilders.BodyBuilder;
import webserver.http.bodybuilders.PlainTextBodyBuilder;
import webserver.http.headers.GeneralHeader;
import webserver.http.headers.Headers;
import webserver.http.headers.SpecificHeader;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import static webserver.Constants.CRLF;

@SuppressWarnings("unchecked")
public class GenericMessage<M extends GenericMessage<M, S, H>, S extends StartLine, H extends SpecificHeader> {
  private final Headers<H> headers = new Headers<>();
  private final Body body = new Body();
  private final S startLine;

  public GenericMessage(S startLine) {
    this.startLine = startLine;
  }

  public Headers<H> headers(){
    return headers;
  }

  public boolean containsHeader(H header) {
    return headers.contains(header);
  }

  public boolean containsHeader(GeneralHeader header) {
    return headers.contains(header);
  }

  public String getHeader(H header) {
    return headers.get(header);
  }

  public String getHeader(GeneralHeader header) {
    return headers.get(header);
  }

  public M addHeader(H header, String value) {
    headers.add(header, value);
    return (M) this;
  }

  public M addHeader(GeneralHeader header, String value) {
    headers.add(header, value);
    return (M) this;
  }

  public M removeHeader(H header, String value) {
    headers.remove(header);
    return (M) this;
  }

  public M removeHeader(GeneralHeader header, String value) {
    headers.remove(header);
    return (M) this;
  }

  public M addBody(byte[] body) {
    this.body.setBody(body);
    return (M) this;
  }

  public M addBody(BodyBuilder<H> bodyBuilder) {
    this.addBody(bodyBuilder.getBody());
    for (Map.Entry<GeneralHeader, String> entry : bodyBuilder.generalHeaders().entrySet())
      addHeader(entry.getKey(), entry.getValue());
    for (Map.Entry<H, String> entry : bodyBuilder.specificHeaders().entrySet())
      addHeader(entry.getKey(), entry.getValue());
    return (M) this;
  }

  public byte[] getBody() {
    return body.getBody();
  }

  public M removeBody() {
    this.body.setBody(new byte[0]);
    return (M) this;
  }

  public byte[][] getBytes() {
    return new byte[][]{
      startLine.toString().getBytes(StandardCharsets.UTF_8),
      headers.toString().getBytes(StandardCharsets.UTF_8),
      CRLF.getBytes(StandardCharsets.UTF_8),
      body.getBody()
    };
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder
      .append(startLine)
      .append(headers)
      .append(CRLF);

    if (body.getBody() != null)
      builder.append(Arrays.toString(body.getBody()));

    else
      builder.append("[no body]");
    return builder.toString();
  }

  public M addBody(String s) {
    addBody(new PlainTextBodyBuilder<>(s));
    return (M) this;
  }

  public S getStartLine() {
    return startLine;
  }
}
