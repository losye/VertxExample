package step.step3;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import step.step3.db.WikiDatabaseVerticle;

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/6/24 下午7:03
 */
public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        Future<String> dbVerticleDeployment = Future.future();
        vertx.deployVerticle(new WikiDatabaseVerticle(), dbVerticleDeployment.completer());

        dbVerticleDeployment.compose(id -> {

            Future<String> httpVerticleDeployment = Future.future();
            vertx.deployVerticle(
                    "io.vertx.guides.wiki.http.HttpServerVerticle",
                    new DeploymentOptions().setInstances(2),
                    httpVerticleDeployment.completer());

            return httpVerticleDeployment;

        }).setHandler(ar -> {
            if (ar.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(ar.cause());
            }
        });
    }
}
