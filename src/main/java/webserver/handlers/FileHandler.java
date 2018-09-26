package webserver.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.WebServerSettings;
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
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler extends WebConnectionRunnable {
  private final Socket socket;
  private final Logger logger = LoggerFactory.getLogger(FileHandler.class);
  private final Path rootDir = Paths.get((String) WebServerSettings.instance().properties.get("dir")).normalize().toAbsolutePath();

  public FileHandler(Socket socket) {
    super(socket);
    this.socket = socket;
  }

  @Override
  public void main() throws Exception {
    var request = RequestParser.parse(socket.getInputStream());
    var response = getResponse(request);
    for (byte[] bytes : response.getBytes())
      socket.getOutputStream().write(bytes);
  }

  private ResponseMessage getResponse(RequestMessage request) throws IOException {
    Path filePath = getFilePath(request);
    if (isPathTraversalAttack(filePath))
      return new ResponseMessage(StatusCode._400);

    ResponseMessage response;
    switch (request.getStartLine().requestMethod) {
      case GET:
        response = getFile(filePath);
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
    return response;
  }

  private boolean isPathTraversalAttack(Path filePath) {
    return !filePath.startsWith(rootDir);
  }

  private Path getFilePath(RequestMessage request) {
    Path relative = request.getStartLine().path;
    return Paths.get(rootDir.toString(), relative.toString());
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

  public ResponseMessage getFile(Path path) throws IOException {
    var response = createResponse(path);
    if (response.getStartLine().statusCode != StatusCode._200)
      return response;
    return response.addBody(new FileBodyBuilder(path));
  }

  private ResponseMessage createResponse(Path path) {
    if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS))
      return new ResponseMessage(StatusCode._404);
    if (!Files.isReadable(path))
      return new ResponseMessage(StatusCode._403);
    return new ResponseMessage(StatusCode._200);
  }
}