package example

import io.vertx.core.Vertx
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import verticle.MyVerticle


/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/5/5 下午2:46
 */
@RunWith(VertxUnitRunner::class)
class MyFirstVerticleTest{
    private lateinit var vertx : Vertx

    @Before
    fun setUp(context: TestContext) {
        vertx = Vertx.vertx()
        vertx.deployVerticle(MyVerticle::class.java.name, context.asyncAssertSuccess())
    }

    @After
    fun tearDown(context: TestContext) {
        vertx.close(context.asyncAssertSuccess())
    }

    @Test
    fun testApplication(context: TestContext) {
        val async = context.async()

        vertx.createHttpClient().getNow(8080, "localhost", "/") { response ->
            response.handler { body ->
                context.assertTrue(body.toString().contains("Hello"))
                async.complete()
            }
        }
    }

}