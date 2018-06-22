package web.client

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.HttpResponse
import io.vertx.ext.web.codec.BodyCodec


/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/5/10 下午3:46
 */

class Webclient : AbstractVerticle() {

    override fun start() {
        val options = WebClientOptions()
                .setUserAgent("My-App/1.2.3")
        options.isKeepAlive = false
        val webClient = WebClient.create(vertx, options)

        webClient//.head()
                .get(8080, "myserver.mycompany.com", "/some-uri")
                .send({ ar ->
                    if (ar.succeeded()) {
                        // 获取响应
                        val response = ar.result()
                        println(response.bodyAsJsonObject())
                    } else {
                        println("err")
                    }
                })

        webClient.head(8080, "myserver.mycompany.com", "/some-uri")
                //.ssl(true)
                .putHeader("content-type", "application/json")
                .putHeader("other-header", "foo")
                .timeout(5000)
                .addQueryParam("param1", "param_value1")
                .addQueryParam("param2", "param_value2")
                /*.putHeader("content-length", fileLen)
                   .sendStream(fileStream, ar -> {
                   if (ar.succeeded()) {
                       // Ok
                   }
                });*/
                //.sendBuffer()
                .`as`(BodyCodec.json(User::class.java))
                .sendJson(hashMapOf("one" to 1), {
                    ar ->
                    if (ar.succeeded()) {
                        // 获取响应
                        val response = ar.result()
                        println(response.bodyAsJsonObject())

                        //if use BodyCodec then result is json
                        val body: User = ar.result().body()
                    } else {
                        println("err, ${ar.cause().message}")
                    }
                })
                /*.sendJsonObject(JsonObject()
                        .put("firstName", "Dale")
                        .put("lastName", "Cooper"), {})*/

    }


}

data class User(val age:Int, val name:String)

