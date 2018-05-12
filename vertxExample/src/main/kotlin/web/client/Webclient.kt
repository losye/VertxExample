package web.client

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.core.json.JsonObject




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
                .putHeader("content-type", "application/json")
                .putHeader("other-header", "foo")
                .addQueryParam("param1", "param_value1")
                .addQueryParam("param2", "param_value2")
                /*.putHeader("content-length", fileLen)
                   .sendStream(fileStream, ar -> {
                   if (ar.succeeded()) {
                       // Ok
                   }
                });*/
                //.sendBuffer()
                .sendJson(hashMapOf("one" to 1), {})
                /*.sendJsonObject(JsonObject()
                        .put("firstName", "Dale")
                        .put("lastName", "Cooper"), {})*/
               //.send({})

    }


}

fun main(args: Array<String>) {

}
