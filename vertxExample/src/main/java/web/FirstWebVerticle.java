package web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.TimeoutHandler;

import java.util.Map;

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/5/7 上午11:10
 */
public class FirstWebVerticle extends AbstractVerticle {

    @Override
    public void start() {
        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        router.route().handler(TimeoutHandler.create(5000));

        router.post("/hello").handler(context -> {
            Map<String, Object> body = context.getBodyAsJson().getMap();
            body.forEach((k, v) -> System.out.println("key:" + k + ", value:" + v));
        });


        httpServer.requestHandler(router::accept).listen(8089);

    }
}
