package web

import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/5/7 上午10:41
 */
class MyFirstWebVerticle : AbstractVerticle() {

    override fun start() {
        val server = vertx.createHttpServer()

        val router = Router.router(vertx)

        router.route("/ping/")
                .method(HttpMethod.GET)
                .method(HttpMethod.POST)
                .method(HttpMethod.DELETE)
                .handler({ routingContext ->
                    routingContext.response()
                            .putHeader("content-type", "text/plain")
                            .end("Hello World from Vert.x-Web")
                })

        router.get("/ping/path").handler({ routingContext ->
            routingContext.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello World from Vert.x-Web! with GET")
        })

        router.get("/path/b").handler({ routingContext ->
            routingContext.reroute("/ping/path")
        })

        router.get("/pipline/").handler({ routingContext ->
            val value: String = routingContext["foo"]
            routingContext
                    .response()
                    .write("$value route1\n")

            routingContext.next()
        })

        router.get("/pipline/").order(-1).handler({ routingContext ->
            routingContext
                    .put("foo", "bar")
                    .response()
                    .setChunked(true)
                    .write("route0\n")
            routingContext.next()
        })

        router.get("/pipline/").handler({ routingContext ->
            routingContext.response()
                    .write("route3\n")
                    .end()
        })

        router.get("/pipline/").failureHandler({ context ->
            val statusCode = context.statusCode()
            context.response().setStatusCode(statusCode).end("Sorry! Not yet")

        })


        server.requestHandler(router::accept).listen(8081)
    }


}