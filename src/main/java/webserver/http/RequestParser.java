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
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import static webserver.Constants.CRLF;

public final class RequestParser {
  private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

  private RequestParser() {
  }

  public static RequestMessage parse(InputStream ins) {
    try {
      var tins = typedInputStream(ins);
      var request = new RequestMessage(parseRequestLine(tins));
      parseHeaders(tins, request);
      parseBody(ins, request);
      return request;
    } catch (Exception e) {
      logger.error("Failed to parse HTTP response message");
      e.printStackTrace();
      return null;
    }
  }

  private static RequestLine parseRequestLine(Scanner ins) throws URISyntaxException {
    var line = CRLF;
    while ((line = ins.nextLine()).equals(CRLF)) ;
    var rl = line.split(" ");
    var method = RequestMethod.valueOf(rl[0]);
    var uri = new URI(rl[1]);
    var httpversion = HttpVersion.getVersion(rl[2].toLowerCase());
    return new RequestLine(method, uri, httpversion);
  }

  private static void parseHeaders(Scanner tins, RequestMessage request) {
    String line;
    while (!(line = tins.nextLine()).equals("")) {
      var headerline = line.split(":");
      var fieldName = headerline[0].toLowerCase();
      var fieldValue = headerline[1].trim();
      GeneralHeader generalHeader;
      RequestHeaders specificHeader;
      if ((generalHeader = GeneralHeaders.getHeader(fieldName)) != null)
        request.addHeader(generalHeader, fieldValue);
      else if ((generalHeader = EntityHeaders.getHeader(fieldName)) != null)
        request.addHeader(generalHeader, fieldValue);
      else if ((specificHeader = RequestHeaders.getHeader(fieldName)) != null)
        request.addHeader(specificHeader, fieldValue);
      else
        logger.warn(String.format("Header field name not recognized: %s", fieldName));
    }
  }

  private static void parseBody(InputStream ins, RequestMessage request) throws IOException {
    if (!request.headers().contains(GeneralHeaders.TransferEncoding) &&
      !request.headers().contains(EntityHeaders.ContentLength))
      return;
    int contentLength = Integer.parseInt(request.headers().get(EntityHeaders.ContentLength));
    var body = new byte[contentLength];
    var total = 0;
    while (total < contentLength)
      total += ins.readNBytes(body, total, contentLength - total);
    request.addBody(body);
  }

  private static Scanner typedInputStream(InputStream ins) {
    return new Scanner(new BufferedInputStream(ins));
  }


}
