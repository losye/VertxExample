package web

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/5/9 下午4:57
 */
class BodyHandlerVerticle : AbstractVerticle(){

    override fun start() {
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)


        router.route().handler(BodyHandler.create())


        router.post("body").handler({
            context ->
            val body = context.bodyAsJson.map
            body.forEach({
                (key, value) -> println("key:$key, value:$value")
            })

        })
    }

}