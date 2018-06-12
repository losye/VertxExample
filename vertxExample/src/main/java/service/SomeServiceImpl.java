package service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/6/11 下午4:14
 */
public class SomeServiceImpl implements SomeService {

    public SomeServiceImpl(Vertx vertx, JsonObject config) {
    }

    @Override
    public SomeService process(String id, Handler<AsyncResult<JsonObject>> resultHandler) {
        return null;
    }
}
