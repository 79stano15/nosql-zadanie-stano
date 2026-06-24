package sk.upjs.nosql.redischat;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class SubscriberService extends Service<Void> {

    ObservableList<String> spravy;

    public SubscriberService(ObservableList<String> spravy) {
        this.spravy = spravy;
    }
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            private RedisConnection connection = RedisConfig.INSTANCE.redisConnection();
            private StringRedisSerializer serializer = new StringRedisSerializer();

            @Override
            protected Void call() throws Exception {
                connection.subscribe(new MessageListener() {
                    @Override
                    public void onMessage(Message message, byte @Nullable [] pattern) {
                        Platform.runLater(() -> spravy.add(message.toString()));
                    }
                }, serializer.serialize(MainSceneController.MESSAGES_CHANNEL));
                return null;
            }
        };
    }
}
