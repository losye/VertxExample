package core.server

import io.vertx.core.Vertx
import core.verticle.MyVerticle
import web.MyFirstWebVerticle

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/5/5 下午2:21
 */
fun main(args: Array<String>) {
    val vertx = Vertx.vertx()
    vertx.deployVerticle(MyVerticle::class.java.name)
    vertx.deployVerticle(MyFirstWebVerticle::class.java.name)
}