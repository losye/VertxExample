package step.step3.db;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.serviceproxy.ProxyHelper;

import java.util.HashMap;

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/6/24 下午7:06
 */
@ProxyGen
@VertxGen
public interface WikiDatabaseService {

    static WikiDatabaseService create(JDBCClient dbClient, HashMap<SqlQuery, String> sqlQueries, Handler<AsyncResult<WikiDatabaseService>> readyHandler) {
        return new WikiDatabaseServiceImpl(dbClient, sqlQueries, readyHandler);
    }

    static WikiDatabaseService createProxy(Vertx vertx, String address) {
        //return new WikiDatabaseServiceVertxEBProxy(vertx, address);
        return ProxyHelper.createProxy(WikiDatabaseService.class, vertx, address);
    }

    @Fluent
    WikiDatabaseService fetchAllPages(Handler<AsyncResult<JsonArray>> resultHandler);

    @Fluent
    WikiDatabaseService fetchPage(String name, Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    WikiDatabaseService createPage(String title, String markdown, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    WikiDatabaseService savePage(int id, String markdown, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    WikiDatabaseService deletePage(int id, Handler<AsyncResult<Void>> resultHandler);


}
