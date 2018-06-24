package step.step2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/6/24 下午3:44
 */
public class MainVerticle extends AbstractVerticle{

        @Override
        public void start(Future<Void> startFuture) {

            Future<String> dbVerticleDeployment = Future.future();
            vertx.deployVerticle(new WikiDatabaseVerticle(), dbVerticleDeployment.completer());

            dbVerticleDeployment.compose(id -> {

                Future<String> httpVerticleDeployment = Future.future();
                vertx.deployVerticle(
                        "io.vertx.guides.wiki.HttpServerVerticle",
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
