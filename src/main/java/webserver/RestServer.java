package webserver;

import webserver.http.message.RequestMessage;
import webserver.http.message.ResponseMessage;
import webserver.http.message.StatusCode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RestServer {
  private final WebServer server;
  private final Map<Map.Entry<String, String>, Function<RequestMessage, ResponseMessage>> processors = new HashMap<>();

  public RestServer() throws IOException {
    this(8080);
  }

  public RestServer(int port) throws IOException {
    this.server = new WebServer(port, new RestProcessor());
  }

  public RestServer get(String uri, Function<RequestMessage, ResponseMessage> processor) {
    return method("GET", uri, processor);
  }

  public RestServer put(String uri, Function<RequestMessage, ResponseMessage> processor) {
    return method("PUT", uri, processor);
  }

  public RestServer post(String uri, Function<RequestMessage, ResponseMessage> processor) {
    return this.method("POST", uri, processor);
  }

  public RestServer delete(String uri, Function<RequestMessage, ResponseMessage> processor) {
    return this.method("DELETE", uri, processor);
  }

  public RestServer head(String uri, Function<RequestMessage, ResponseMessage> processor) {
    return this.method("HEAD", uri, processor);
  }

  public RestServer connect(String uri, Function<RequestMessage, ResponseMessage> processor) {
    return this.method("CONNECT", uri, processor);
  }

  public RestServer options(String uri, Function<RequestMessage, ResponseMessage> processor) {
    return this.method("OPTIONS", uri, processor);
  }

  public RestServer trace(String uri, Function<RequestMessage, ResponseMessage> processor) {
    return this.method("TRACE", uri, processor);
  }

  private RestServer method(String method, String uri, Function<RequestMessage, ResponseMessage> processor) {
    processors.put(Map.entry(method, uri), processor);
    return this;
  }

  private class RestProcessor implements Function<RequestMessage, ResponseMessage> {
    @Override
    public ResponseMessage apply(RequestMessage requestMessage) {
      var startline = requestMessage.getStartLine();
      var entry = Map.entry(startline.requestMethod.name(), startline.path.toString().replace("\\", "/"));
      var processor = processors.get(entry);
      if (processor != null)
        return processor.apply(requestMessage);
      return new ResponseMessage(StatusCode._404);
    }
  }
}
