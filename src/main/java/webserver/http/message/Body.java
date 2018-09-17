package webserver.http.message;

import java.util.Arrays;

public class Body {
  private byte[] entity;

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
