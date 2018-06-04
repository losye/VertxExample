package service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.HttpEndpoint;

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/6/4 上午10:37
 */
public class Discovery extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Discovery.class.getName());
    }

    @Override
    public void start() throws Exception {
        // Customize the configuration
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx,
                new ServiceDiscoveryOptions()
                        .setAnnounceAddress("service-announce")
                        .setName("my-name"));

        Record endpoint1 = new Record()
                .setType("eventbus-service-proxy")
                .setLocation(new JsonObject().put("endpoint", "the-service-address"))
                .setName("my-service")
                .setMetadata(new JsonObject().put("some-label", "some-value"));

        //publish "my-service" service
        discovery.publish(endpoint1, ar -> {
            if (ar.succeeded()) {
                System.out.println("\"" + endpoint1.getName() + "\" successfully published!");
                Record publishedRecord = ar.result();
            } else {
                // publication failed
            }
        });

        Record endpoint2 = HttpEndpoint.createRecord("some-rest-api", "localhost", 8080, "/api");


        discovery.publish(endpoint2, ar -> {
            if (ar.succeeded()) {
                System.out.println("\"" + endpoint2.getName() + "\" successfully published!");
                Record publishedRecord = ar.result();
            } else {
                // publication failed
            }
        });

        //unpublish "my-service"
        discovery.unpublish(endpoint1.getRegistration(), ar -> {
            if (ar.succeeded()) {
                System.out.println("\"" + endpoint1.getName() + "\" successfully unpublished");
            } else {
                // cannot un-publish the service, may have already been removed, or the record is not published
            }
        });

        //consuming a service
        discovery.getRecord(r -> r.getName().equals(endpoint2.getName()), ar -> {
            if (ar.succeeded()) {
                if (ar.result() != null) {
                    // Retrieve the service reference
                    ServiceReference reference = discovery.getReference(ar.result());
                    // Retrieve the service object
                    HttpClient client = reference.get();
                    System.out.println("Consuming \"" + endpoint2.getName() + "\"");

                    client.getNow("/api", response -> {
                        //release the service
                        reference.release();

                    });
                }
            }

        });

        discovery.close();

    }
}
