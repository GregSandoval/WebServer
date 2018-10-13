package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.headers.EntityHeaders;
import webserver.http.headers.GeneralHeader;
import webserver.http.headers.GeneralHeaders;
import webserver.http.headers.RequestHeaders;
import webserver.http.message.RequestLine;
import webserver.http.message.RequestMessage;
import webserver.http.message.RequestMethod;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;

import static webserver.Constants.CRLF;

public final class RequestParser {
  private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

  private RequestParser() {
  }

  public static Optional<RequestMessage> parse(BufferedInputStream ins, Scanner tins) {
    try {
      var request = new RequestMessage(parseRequestLine(tins));
      parseHeaders(tins, request);
      parseBody(ins, request);
      return Optional.of(request);
    } catch (Exception e) {
      logger.info("Failed to parse HTTP response message: " + e + " -> " + e.getStackTrace()[0]);
      return Optional.empty();
    }
  }

  private static RequestLine parseRequestLine(Scanner ins) {
    var line = CRLF;
    while ((line = ins.nextLine()).equals(CRLF)) ;
    var rl = line.split(" ");
    var method = RequestMethod.valueOf(rl[0]);
    var uri = Paths.get(rl[1]).normalize();
    var httpversion = HttpVersion.getVersion(rl[2].toLowerCase());
    return new RequestLine(method, uri, httpversion);
  }

  private static void parseHeaders(Scanner tins, RequestMessage request) {
    String line;
    while (!(line = tins.nextLine()).equals("")) {
      var headerline = line.split(":", 2);
      var fieldName = headerline[0].toLowerCase();
      var fieldValue = headerline[1].trim().split(",");
      GeneralHeader generalHeader;
      RequestHeaders specificHeader;
      if ((generalHeader = GeneralHeaders.getHeader(fieldName)) != null)
        request.addHeaders(generalHeader, fieldValue);
      else if ((generalHeader = EntityHeaders.getHeader(fieldName)) != null)
        request.addHeaders(generalHeader, fieldValue);
      else if ((specificHeader = RequestHeaders.getHeader(fieldName)) != null)
        request.addHeaders(specificHeader, fieldValue);
      else
        logger.warn("Header field name not recognized: " + fieldName);
    }
  }

  private static void parseBody(BufferedInputStream ins, RequestMessage request) throws IOException {
    if (!request.containsHeader(GeneralHeaders.TransferEncoding) &&
      !request.containsHeader(EntityHeaders.ContentLength))
      return;
    int contentLength = Integer.parseInt(request.getHeader(EntityHeaders.ContentLength).get(0));
    var body = new byte[contentLength];
    var total = 0;
    while (total < contentLength)
      total += ins.read(body, total, contentLength - total);
    request.addBody(body);
  }
}
