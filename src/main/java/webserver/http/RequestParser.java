package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.headers.*;
import webserver.http.message.Body;
import webserver.http.message.RequestLine;
import webserver.http.message.RequestMessage;
import webserver.http.message.RequestMethod;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static webserver.Constants.CRLF;

public final class RequestParser {
  private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

  private RequestParser() {
  }

  public static RequestMessage parse(InputStream ins) {
    try {
      var tins = typedInputStream(ins);
      var requestLine = parseRequestLine(tins);
      var headers = parseHeaders(tins);
      var body = parseBody(ins, headers);
      return new RequestMessage(requestLine, headers, body);
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

  private static Headers<RequestHeaders> parseHeaders(Scanner tins) {
    var headers = new Headers<RequestHeaders>();
    String line;
    while (!(line = tins.nextLine()).equals("")) {
      var headerline = line.split(":");
      var fieldName = headerline[0].toLowerCase();
      var fieldValue = headerline[1].trim();
      GeneralHeader generalHeader;
      RequestHeaders specificHeader;
      if ((generalHeader = GeneralHeaders.getHeader(fieldName)) != null)
        headers.add(generalHeader, fieldValue);
      else if ((generalHeader = EntityHeaders.getHeader(fieldName)) != null)
        headers.add(generalHeader, fieldValue);
      else if ((specificHeader = RequestHeaders.getHeader(fieldName)) != null)
        headers.add(specificHeader, fieldValue);
      else
        logger.warn(String.format("Header field name not recognized: %s", fieldName));
    }
    return headers;
  }

  private static Body parseBody(InputStream ins, Headers<RequestHeaders> headers) throws IOException {
    if (!headers.contains(GeneralHeaders.TransferEncoding) &&
      !headers.contains(EntityHeaders.ContentLength))
      return new Body("");
    int contentLength = Integer.parseInt(headers.get(EntityHeaders.ContentLength));
    var body = new byte[contentLength];
    var total = 0;
    while (total < contentLength)
      total += ins.readNBytes(body, total, contentLength - total);
    return new Body(new String(body, StandardCharsets.UTF_8));
  }

  private static Scanner typedInputStream(InputStream ins) {
    return new Scanner(new BufferedInputStream(ins));
  }


}
