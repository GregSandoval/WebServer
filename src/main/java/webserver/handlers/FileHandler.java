package webserver.handlers;

import webserver.http.RequestParser;
import webserver.http.bodybuilders.FileBodyBuilder;
import webserver.http.headers.EntityHeaders;
import webserver.http.message.RequestMessage;
import webserver.http.message.ResponseMessage;
import webserver.http.message.StatusCode;
import webserver.threading.WebConnectionRunnable;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler extends WebConnectionRunnable {
  private final Socket socket;

  public FileHandler(Socket socket) {
    super(socket);
    this.socket = socket;
  }

  @Override
  public void main() throws IOException {
    var request = RequestParser.parse(socket.getInputStream());
    ResponseMessage response;
    switch (request.getStartLine().requestMethod) {
      case GET:
        response = getFile(request);
        break;
      case HEAD:
        // response = getFileHead(request);
        // break;
      case POST:
        // response = postFile(request);
        // break;
      case PUT:
        // response = putFile(request);
        // break;
      case DELETE:
        // response = deleteFile(request);
        // break;
      case TRACE:
        // response = trace(request);
        // break;
      case OPTIONS:
        // response = fileOptions(request);
        // break;
      case CONNECT: // All fall through
      default:
        response = new ResponseMessage(StatusCode._405);
        response.addHeader(EntityHeaders.Allow, "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, CONNECT");
    }
    for (byte[] bytes : response.getBytes())
      socket.getOutputStream().write(bytes);
  }

  private ResponseMessage fileOptions(RequestMessage request) {
    return null;
  }

  private ResponseMessage trace(RequestMessage request) {
    return null;
  }

  private ResponseMessage deleteFile(RequestMessage request) {
    return null;
  }

  private ResponseMessage putFile(RequestMessage request) {
    return null;
  }

  private ResponseMessage postFile(RequestMessage request) {
    return null;
  }

  private ResponseMessage getFileHead(RequestMessage request) {
    return null;
  }

  public ResponseMessage getFile(RequestMessage request) throws IOException {
    var path = request.getStartLine().path;
    var response = createResponse(path);
    if (response.getStartLine().statusCode != StatusCode._200)
      return response;
    return response.addBody(new FileBodyBuilder(path));
  }

  private ResponseMessage createResponse(Path path) {
    System.out.println(path.toAbsolutePath());
    if (!Files.isRegularFile(path))
      return new ResponseMessage(StatusCode._404);
    if (!Files.isReadable(path))
      return new ResponseMessage(StatusCode._403);
    return new ResponseMessage(StatusCode._200);
  }
}
