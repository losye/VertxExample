package step;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/6/24 下午3:55
 */
public class DemoUtil {
    private static DemoUtil ourInstance = new DemoUtil();

    public static DemoUtil getInstance() {
        return ourInstance;
    }

    private DemoUtil() {
    }

    @NotNull
    public static Handler<AsyncResult<Buffer>> httpResultHandler(RoutingContext context) {
        return ar -> {
            if (ar.succeeded()) {
                context.response().putHeader("Content-Type", "text/html");
                context.response().end(ar.result());
            } else {
                context.fail(ar.cause());
            }
        };
    }
}
