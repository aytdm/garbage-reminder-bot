package com.mn.linebot.garbagereminder.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.extern.slf4j.Slf4j;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Service;
import com.mn.linebot.garbagereminder.config.LineProperties;

@Slf4j
@Service
public class PushConfirmService {

    private final LineProperties lineProperties;
    private final LineMessagingClient lineMessagingClient;

    PushConfirmService(LineProperties lineProperties, LineMessagingClient lineMessagingClient) {
        this.lineProperties = lineProperties;
        this.lineMessagingClient = lineMessagingClient;
    }

    public void pushBurnablesAlarm() throws URISyntaxException {
        try {
            BotApiResponse response = lineMessagingClient
                                            .pushMessage(
                                                    new PushMessage(
                                                            lineProperties.getId(),
                                                            new TemplateMessage(
                                                                    "Tomorrow is the garbage day for burnables！",
                                                                    new ConfirmTemplate("Did you take out the garbage?",
                                                                            new MessageAction("yes", "yes"),
                                                                            new MessageAction("no", "no")
                                                                    )
                                                            )
                                                    )
                                            )
                                            .get();
            log.info("Sent messages: {}", response);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
