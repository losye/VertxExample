package jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/6/6 下午2:50
 */
public class JdbcVerticle extends AbstractVerticle {

    private JDBCClient jdbcClient;


    @Override
    public void start(Future<Void> startFuture) {
        jdbcClient = JDBCClient.createShared(vertx, config(), "dataSourceName");

        startBackend((nil) -> startWebApp(
                (http) -> completeStartup(http, startFuture)
        ), startFuture);
    }

    private void startBackend(Handler<AsyncResult<SQLConnection>> next, Future<Void> future) {
        jdbcClient.getConnection(ar -> {
            if (ar.failed()) {
                future.fail(ar.cause());
            } else {
                next.handle(Future.succeededFuture(ar.result()));
            }
        });
    }

    private void completeStartup(AsyncResult<HttpServer> http, Future<Void> future) {
        if (http.succeeded()) {
            future.complete();
        } else {
            future.fail(http.cause());
        }
    }

    private void startWebApp(Handler<AsyncResult<HttpServer>> next) {
        Router router = Router.router(vertx);

        router.route("/").handler(routingContext ->
                routingContext.response()
                        .putHeader("content-type", "text/html")
                        .end("<h1>Hello from my first Vert.x 3 application</h1>"));

        router.get("/api/whiskies/").handler(this::getAll);
        router.post("/api/whiskies").handler(this::addOne);
        router.get("/api/whiskies/:id").handler(this::getOne);
        router.put("/api/whiskies/:id").handler(this::updateOne);
        router.delete("/api/whiskies/:id").handler(this::deleteOne);


        // Create the HTTP server and pass the "accept" method to the request handler.
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        // Retrieve the port from the configuration,
                        // default to 8080.
                        config().getInteger("http.port", 8080),
                        next
                );
    }

    private void getAll(RoutingContext routingContext) {
        jdbcClient.getConnection(ar -> {
            SQLConnection connection = ar.result();
            connection.query("SELECT * FROM whisky", result -> {
                List<Whisky> whiskyList = result.result().getRows().stream()
                        .map(Whisky::new)
                        .collect(toList());
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(whiskyList));

                connection.close();
            });
        });
    }

    private void deleteOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        checkParam(id, routingContext);
        jdbcClient.getConnection(ar -> {
            SQLConnection connection = ar.result();
            connection.updateWithParams("delete from whisky where id = ?",
                    new JsonArray().add(id),
                    resultAsyncResult -> {
                        if (resultAsyncResult.failed()) {
                            routingContext.response().setStatusCode(500).end();
                        } else {
                            routingContext.response().setStatusCode(200).end();
                        }
                        connection.close();
                    }
            );

        });
    }


    private void updateOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        JsonObject json = routingContext.getBodyAsJson();
        checkParam(id, routingContext);
        jdbcClient.getConnection(ar -> {
            SQLConnection connection = ar.result();
            String sql = "UPDATE Whisky SET name=?, origin=? WHERE id=?";
            connection.updateWithParams(sql,
                    new JsonArray().add(json.getValue("name")).add(json.getValue("origin")),
                    result -> {
                        if (result.failed()) {
                            routingContext.response()
                                    .setStatusCode(400)
                                    .end();
                        } else {
                            routingContext.response()
                                    .putHeader("content-type", "application/json; charset=utf-8")
                                    .end(result.result().toString());
                        }
                        connection.close();
                    });
        });
    }

    private void getOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        checkParam(id, routingContext);
        jdbcClient.getConnection(ar -> {
            SQLConnection connection = ar.result();
            selectById(id, connection, result -> {
                if (result.succeeded()) {
                    routingContext.response()
                            .setStatusCode(200)
                            .putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(result.result()));
                } else {
                    routingContext.response()
                            .setStatusCode(500)
                            .end();
                }
                connection.close();
            });
        });

    }

    private void selectById(String id, SQLConnection sqlConnection, Handler<AsyncResult<Whisky>> resultHandler) {
        sqlConnection.queryWithParams("select * from whisky where id = ?",
                new JsonArray().add(id),
                ar -> {
                    if (ar.failed()) {
                        resultHandler.handle(Future.failedFuture("select whisky failed"));
                    } else {
                        resultHandler.handle(Future.succeededFuture(new Whisky(ar.result().getRows().get(0))));
                    }
                });
    }

    private void addOne(RoutingContext routingContext) {
        jdbcClient.getConnection(ar -> {
            final Whisky whisky = Json.decodeValue(routingContext.getBodyAsString(), Whisky.class);
            SQLConnection connection = ar.result();
            insertWhisky(whisky, connection, result -> {
                if (result.succeeded()) {
                    routingContext.response()
                            .setStatusCode(200)
                            .putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(result.result()));
                } else {
                    routingContext.response()
                            .setStatusCode(500)
                            .end();
                }
                connection.close();
            });

        });
    }

    private void insertWhisky(Whisky whisky, SQLConnection connection, Handler<AsyncResult<Whisky>> next) {
        String sql = "INSERT INTO Whisky (name, origin) VALUES ?, ?";
        connection.updateWithParams(sql,
                new JsonArray().add(whisky.getName()).add(whisky.getOrigin()),
                ar -> {
                    if (ar.failed()) {
                        next.handle(Future.failedFuture(ar.cause()));
                    } else {
                        next.handle(Future.succeededFuture(new Whisky(ar.result().toJson())));
                    }
                });
    }

    private void checkParam(String value, RoutingContext routingContext) {
        if (value == null || "".equals(value)) {
            routingContext.response().setStatusCode(400).end();
        }
    }


}
