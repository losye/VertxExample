package step;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.runner.RunWith;
import step.step3.db.WikiDatabaseService;
import step.step3.db.WikiDatabaseVerticle;

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/6/24 下午8:57
 */
@RunWith(VertxUnitRunner.class)
public class WikiDatabaseVerticleTest {

    private Vertx vertx;
    private WikiDatabaseService service;

    @Before
    public void prepare(TestContext context) throws InterruptedException {
        vertx = Vertx.vertx();

        JsonObject conf = new JsonObject()  // <1>
                .put(WikiDatabaseVerticle.CONFIG_WIKIDB_JDBC_URL, "jdbc:hsqldb:mem:testdb;shutdown=true")
                .put(WikiDatabaseVerticle.CONFIG_WIKIDB_JDBC_MAX_POOL_SIZE, 4);

        vertx.deployVerticle(new WikiDatabaseVerticle(), new DeploymentOptions().setConfig(conf),
                context.asyncAssertSuccess(id ->  // <2>
                        service = WikiDatabaseService.createProxy(vertx, WikiDatabaseVerticle.CONFIG_WIKIDB_QUEUE)));
    }
}
