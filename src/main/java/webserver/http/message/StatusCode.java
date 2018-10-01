package webserver.http.message;

public enum StatusCode {
  _100("Continue"),
  _101("Switching Protocols"),
  _200("OK"),
  _201("Created"),
  _203("Non-Authoritative Information"),
  _204("No Content"),
  _202("Accepted"),
  _205("Reset Content"),
  _206("Partial Content"),
  _300("Multiple Choices"),
  _301("Moved Permanently"),
  _302("Found"),
  _303("See Other"),
  _304("Not Modified"),
  _305("Use Proxy"),
  _400("Bad Request"),
  _401("Unauthorized"),
  _307("Temporary Redirect"),
  _402("Payment Required"),
  _403("Forbidden"),
  _404("Not Found"),
  _405("Method Not Allowed"),
  _406("Not Acceptable"),
  _407("Proxy Authentication Required"),
  _408("Request Time-out"),
  _409("Conflict"),
  _410("Gone"),
  _411("Length Required"),
  _412("Precondition Failed"),
  _413("Request Entity Too Large"),
  _414("Request-URI Too Large"),
  _415("Unsupported Media Type"),
  _416("Requested range not satisfiable"),
  _417("Expectation Failed"),
  _500("Internal Server Error"),
  _501("Not Implemented"),
  _502("Bad Gateway"),
  _503("Service Unavailable"),
  _504("Gateway Time-out"),
  _505("HTTP Version not supported");

  public final String description;
  private final int code;

  StatusCode(String description) {
    this.description = description;
    this.code = Integer.parseInt(name().substring(1));
  }

  public boolean isInformationalCode() {
    return code < 200;
  }

  public boolean isSuccessCode() {
    return code >= 200 && code < 300;
  }

  public boolean isRedirectionCode() {
    return code >= 300 && code < 400;
  }

  public boolean isClientError() {
    return code >= 400 && code < 500;
  }

  public boolean isServerError() {
    return code >= 500 && code < 600;
  }

  public int toCode() {
    return code;
  }

  @Override
  public String toString() {
    return description;
  }
}
