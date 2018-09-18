package webserver.http.message;

public class Body {
  private byte[] entity = new byte[0];

  public Body() {
  }

  public Body(byte[] entity) {
    this.entity = entity;
  }

  public void setBody(byte[] entity) {
    this.entity = entity;
  }


  public byte[] getBody() {
    return this.entity;
  }
}
