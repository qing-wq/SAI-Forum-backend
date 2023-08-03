package ink.whi.core.rabbitmq;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * RabbitMq 连接池
 *
 * @author: qing
 * @Date: 2023/7/31
 */
public class RabbitmqConnectionPool {

    private static BlockingQueue<RabbitmqConnection> pool;

    /**
     * 初始化连接池
     * @param host
     * @param port
     * @param userName
     * @param password
     * @param virtualhost
     * @param poolSize 最大连接数
     */
    public static void initRabbitmqConnectionPool(String host, int port, String userName, String password,
                                                  String virtualhost,
                                                  Integer poolSize) {
        pool = new LinkedBlockingQueue<>(poolSize);    // 线程安全的阻塞队列
        for (int i = 0; i < poolSize; i++) {
            pool.add(new RabbitmqConnection(host, port, userName, password, virtualhost));
        }
    }

    public static RabbitmqConnection getConnection() throws InterruptedException {
        return pool.take();
    }

    public static void returnConnection(RabbitmqConnection connection) {
        pool.add(connection);
    }

    public static void close() {
        pool.forEach(RabbitmqConnection::releaseConnection);
    }
}
