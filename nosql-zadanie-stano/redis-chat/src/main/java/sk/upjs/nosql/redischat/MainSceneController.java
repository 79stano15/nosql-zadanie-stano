package sk.upjs.nosql.redischat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.springframework.data.redis.core.RedisTemplate;


import java.util.ArrayList;

public class MainSceneController {

    public static final String MESSAGES_CHANNEL = "chatChannel";
    @FXML private ListView<String> chatAreaListView;
    @FXML private Button sendButton;
    @FXML private TextField textToSendTextField;
    @FXML private TextField menoTextField;

    private ObservableList<String> spravy;
    private RedisTemplate<String, String> redisTemplate;

    public MainSceneController() {
        redisTemplate = RedisConfig.INSTANCE.redisTemplate();
    }

    @FXML
    void initialize() {
        spravy = FXCollections.observableArrayList(new ArrayList<String>());
        chatAreaListView.setItems(spravy);
        String meno = menoTextField.textProperty().getValue();
        if (meno == null || meno.trim().length() == 0) {
            meno = "user" + (int)(100000*Math.random());
            menoTextField.textProperty().setValue(meno);
        }
        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text = textToSendTextField.getText();
                String name = menoTextField.getText();
                System.out.println("Sending message: " + name + ": " + text);
                redisTemplate.convertAndSend(MESSAGES_CHANNEL, name + ": " + text);
                textToSendTextField.clear();
            }
        });
        SubscriberService service = new SubscriberService(spravy);
        service.start();
    }

}
