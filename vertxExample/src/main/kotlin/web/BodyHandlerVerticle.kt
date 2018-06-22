package web

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.TimeoutHandler

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/5/9 下午4:57
 */
class BodyHandlerVerticle : AbstractVerticle() {

    override fun start() {
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)


        router.route().handler(BodyHandler.create())

        //router.route().handler(CookieHandler.create())

        //router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)))

        //router.route().handler(StaticHandler.create())

        //router.route().handler(CorsHandler.create("vertx\\.io").allowedMethod(HttpMethod.GET))

        router.route().handler(TimeoutHandler.create(5000))

        router.post("body").handler({ context ->
            val body = context.bodyAsJson.map
            body.forEach({ (key, value) ->
                println("key:$key, value:$value")
            })
        })


        server.requestHandler(router::accept).listen(8082)
    }

}