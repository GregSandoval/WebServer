package webserver.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.WebServerSettings;
import webserver.http.bodybuilders.FileBodyBuilder;
import webserver.http.headers.EntityHeaders;
import webserver.http.message.RequestMessage;
import webserver.http.message.ResponseMessage;
import webserver.http.message.StatusCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

public class FileHandler implements Function<RequestMessage, ResponseMessage> {
  private final Logger logger = LoggerFactory.getLogger(FileHandler.class);
  private final Path rootDir = Paths.get((String) WebServerSettings.instance().properties.get("dir")).normalize().toAbsolutePath();

  public FileHandler() {
    logger.info("Serving files located in directory: " + Paths.get((String) WebServerSettings.instance().properties.get("dir")).normalize().toAbsolutePath());
  }

  public ResponseMessage apply(RequestMessage request) {
    Path path = getPath(request);

    if (outsideWebDirectory(path))
      return new ResponseMessage(StatusCode._400);

    if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS))
      return new ResponseMessage(StatusCode._404);

    if (!Files.isReadable(path))
      return new ResponseMessage(StatusCode._403);

    if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS))
      return new ResponseMessage(StatusCode._401);

    ResponseMessage response;
    switch (request.getStartLine().requestMethod) {
      case GET:
        response = getFile(path);
        break;
      case HEAD:
        response = getFileHead(path);
        break;
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

  private boolean outsideWebDirectory(Path filePath) {
    return !filePath.startsWith(rootDir);
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

  private ResponseMessage getFileHead(Path path) {
    try {
      var response = new ResponseMessage(StatusCode._200);
      response.addBody(new FileBodyBuilder(path, false));
      return response;
    } catch (IOException e) {
      logger.error("Trouble reading file: " + path.getFileName() + "; error: " + e.getMessage());
      return new ResponseMessage(StatusCode._500);
    }
  }

  private ResponseMessage getFile(Path path) {
    try {
      var response = new ResponseMessage(StatusCode._200);
      response.addBody(new FileBodyBuilder(path));
      return response;
    } catch (IOException e) {
      logger.error("Trouble reading file: " + path.getFileName() + "; error: " + e.getMessage());
      return new ResponseMessage(StatusCode._500);
    }
  }

  private Path getPath(RequestMessage request) {
    Path relative = request.getStartLine().path;
    return Paths.get(rootDir.toString(), relative.toString());
  }
}