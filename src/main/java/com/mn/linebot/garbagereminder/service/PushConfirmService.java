package com.mn.linebot.garbagereminder.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class PushConfirmService {

  @Value("${garbage.reminder.line_bot_id}")
  private String lineBotId;

  private final LineMessagingClient lineMessagingClient;

  PushConfirmService(LineMessagingClient lineMessagingClient) {
    this.lineMessagingClient = lineMessagingClient;
  }

  public void pushBurnablesAlarm() throws URISyntaxException {
    try {
      BotApiResponse response =
          lineMessagingClient
              .pushMessage(
                  new PushMessage(
                      lineBotId,
                      new TemplateMessage(
                          "Tomorrow is the garbage day for burnables！",
                          new ConfirmTemplate(
                              "Did you take out the garbage?",
                              new MessageAction("yes", "yes"),
                              new MessageAction("no", "no")))))
              .get();
      log.info("Sent messages: {}", response);
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }
}
