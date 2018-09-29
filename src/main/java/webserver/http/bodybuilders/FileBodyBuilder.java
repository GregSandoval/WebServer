package webserver.http.bodybuilders;

import webserver.http.headers.EntityHeaders;
import webserver.http.headers.GeneralHeader;
import webserver.http.headers.ResponseHeaders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileBodyBuilder implements BodyBuilder<ResponseHeaders> {
  private byte[] body;
  private Map<GeneralHeader, String> generalHeaders = new HashMap<>();
  private Map<ResponseHeaders, String> specificHeaders = Map.of();

  public FileBodyBuilder(Path path) throws IOException {
    this(path, true);
  }

  public FileBodyBuilder(Path path, boolean loadFileContents) throws IOException {
    if (loadFileContents)
      body = Files.readAllBytes(path);
    else
      body = new byte[0];
    generalHeaders.put(EntityHeaders.ContentType, Files.probeContentType(path));
    generalHeaders.put(EntityHeaders.ContentLength, String.valueOf(Files.size(path)));
  }

  @Override
  public byte[] getBody() {
    return body;
  }

  @Override
  public Map<GeneralHeader, String> generalHeaders() {
    return generalHeaders;
  }

  @Override
  public Map<ResponseHeaders, String> specificHeaders() {
    return specificHeaders;
  }
}
