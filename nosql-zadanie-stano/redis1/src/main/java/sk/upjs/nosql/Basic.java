package sk.upjs.nosql;

import redis.clients.jedis.RedisClient;

public class Basic {
    public static void main(String[] args) {
        RedisClient client = RedisFactory.INSTANCE.getRedisClient();
        var x = client.get("x");
        System.out.println("X: " + x);
    }
}
