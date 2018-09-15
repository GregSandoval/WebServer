package webserver.http.message;

public class Body {
  public final String entityBody;

  public Body(String entityBody) {
    this.entityBody = entityBody;
  }

  @Override
  public String toString() {
    return entityBody;
  }
}
