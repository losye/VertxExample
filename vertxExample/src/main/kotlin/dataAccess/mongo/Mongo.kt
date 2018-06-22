package dataAccess.mongo

import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient


/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/5/13 上午10:26
 */
class Mongo : AbstractVerticle() {

    override fun start() {
        val mongoClient = MongoClient.createShared(vertx, config(), "myPoolName")

        val document = JsonObject().put("title", "The Hobbit")

        mongoClient.insert("books", document) { res ->

            if (res.succeeded()) {
                val id = res.result()
                println("Inserted book with id: $id")
            } else {
                res.cause().printStackTrace()
            }
        }
    }

}