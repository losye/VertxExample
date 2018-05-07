package core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/5/5 下午5:55
 */
public class FutureCompose extends AbstractVerticle {


    @Override
    public void start() {
        Future.<Message<String>>future(f ->
                vertx.eventBus().send("address1", "message", f)
        ).compose((msg) ->
                Future.<Message<String>>future(f ->
                        vertx.eventBus().send("address2", msg.body(), f)
                )
        ).compose((msg) ->
                Future.<Message<String>>future(f ->
                        vertx.eventBus().send("address3", msg.body(), f)
                )
        ).setHandler((res) -> {
            if (res.failed()) {
                //deal with exception
                throw new RuntimeException();
            }
            //deal with the result
        });


    }
}
