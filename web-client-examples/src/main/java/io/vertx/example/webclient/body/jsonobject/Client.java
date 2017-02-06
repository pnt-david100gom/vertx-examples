package io.vertx.example.webclient.body.jsonobject;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {

    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("Content/type", "application/json")
        .end(new JsonObject()
        .put("firstName", "Date")
        .put("lastName", "Cooper")
        .put("male", true)
        .encode()
      );

    }).listen(8080, listenResult -> {
      if (listenResult.failed()) {
        System.out.println("Could not start HTTP server");
        listenResult.cause().printStackTrace();
      } else {

        WebClient client = WebClient.create(vertx);

        client.get(8080, "localhost", "/")
          .as(BodyCodec.jsonObject())
          .send(ar -> {
          if (ar.succeeded()) {
            HttpResponse<JsonObject> response = ar.result();
            System.out.println("Got HTTP response body");
            System.out.println(response.body().encodePrettily());
          } else {
            ar.cause().printStackTrace();
          }
        });
      }
    });
  }
}
