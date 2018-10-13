package webserver.http;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class RequestParserTest {

  @Test
  public void request() {
    var goodRequest = getGoodRequest();
    var badRequests = new String[]{
      goodRequest.replaceFirst("\r\n", ""),
      goodRequest.replaceFirst("GET", "get"),
      goodRequest.replaceFirst("text/html", "tex/html"),
      goodRequest.replaceFirst("WORLD", ""),
      goodRequest.replaceFirst("WORLD", "WORLD MORE TEXT")
    };
    for (var request : badRequests) {
      try (var bisn = new BufferedInputStream(new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8)))) {
        var scanner = new Scanner(bisn);
        var obj = RequestParser.parse(bisn, scanner);
        assertFalse(obj.isPresent());
      } catch (IOException e) {
        fail("IOException on okay input stream.");
        e.printStackTrace();
      }
    }
  }

  private String getGoodRequest() {
    String body = "Hello World";
    return "GET / HTTP/1.1\r\n" +
      "Content-Type: text/html\r\n" +
      "Content-Length:" + body.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
      "\r\n" +
      body;
  }
}