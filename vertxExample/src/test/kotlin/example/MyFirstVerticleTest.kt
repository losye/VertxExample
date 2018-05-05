package example

import io.vertx.core.Vertx
import io.vertx.ext.unit.TestContext
import org.junit.runner.RunWith
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before
import verticle.MyVerticle
import io.vertx.groovy.ext.unit.Completion_GroovyExtension.handler
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil.close
import org.junit.After
import org.junit.Test


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