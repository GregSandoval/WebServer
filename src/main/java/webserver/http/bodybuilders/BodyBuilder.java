package webserver.http.bodybuilders;

import webserver.http.headers.GeneralHeader;
import webserver.http.headers.SpecificHeader;

import java.util.Map;

public interface BodyBuilder<H extends SpecificHeader> {
  byte[] getBody();

  Map<GeneralHeader, String> generalHeaders();

  Map<H, String> specificHeaders();
}
