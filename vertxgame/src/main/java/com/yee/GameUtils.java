package com.yee;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/6/24 上午11:23
 */
public class GameUtils {
    public static Endpoint retrieveEndpoint(final String env, final int testPort, final String testPath) {
        String endpoint = System.getenv(env);
        Endpoint result;
        if (endpoint == null) {
            result = new Endpoint("localhost", testPort, testPath);
        } else {
            endpoint = endpoint.trim();
            try {
                final URL url = new URL(endpoint);
                final String host = url.getHost();
                int port = url.getPort();
                if (port == -1) {
                    port = url.getDefaultPort();
                }
                final String path = url.getPath();

                result = new Endpoint(host, port, path);
            } catch (final MalformedURLException murle) {
                result = new Endpoint("localhost", testPort, testPath);
            }
        }
        return result;
    }

    public static class Endpoint {
        private final String host;
        private final int port;
        private final String path;

        Endpoint(String host, int port, String path) {
            this.host = host;
            this.port = port;
            this.path = path;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public String getPath() {
            return path;
        }

        @Override
        public String toString() {
            return this.host + ":" + port + "/" + path;
        }

    }  // Endpoint
}
