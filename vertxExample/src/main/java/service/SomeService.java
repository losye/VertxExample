package service;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ProxyHelper;

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/6/11 上午11:23
 */
@ProxyGen
@VertxGen
public interface SomeService {
    String SERVICE_ADDRESS = "service.example";
    static SomeService createService(Vertx vertx, JsonObject config) {
        return new SomeServiceImpl(vertx, config);
    }
    static SomeService createProxy(Vertx vertx) {
        return ProxyHelper.createProxy(SomeService.class, vertx, SERVICE_ADDRESS);
    }
    @Fluent
    SomeService process(String id, Handler<AsyncResult<JsonObject>> resultHandler);

}
