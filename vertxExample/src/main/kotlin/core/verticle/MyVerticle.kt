package core.verticle

import io.vertx.core.AbstractVerticle

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/5/5 下午2:21
 */
class MyVerticle : AbstractVerticle() {

    var i = 0

    override fun start() {
        vertx.createHttpServer()
                .requestHandler { req ->
                    req.response().putHeader("content-type", "text/plain").end("Hello from Vert.x, and val: $i")
                }
                .listen(8080)

        /* vertx.createHttpServer()
                 .requestHandler { req ->
                     i++
                     req.response().end()
                 }
                 .listen(8081)*/
    }

}