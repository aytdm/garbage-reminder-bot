package com.mn.linebot.garbagereminder.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.mn.linebot.garbagereminder.domain.NotificationMessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

/** Push confirm notification service */
@Slf4j
@Service
@RequiredArgsConstructor
public class PushConfirmService {
  private static final String YES = "yes";

  private static final String NO = "no";

  private final LineMessagingClient lineMessagingClient;

  @Value("${garbage.reminder.line_bot_id}")
  private String lineBotId;

  public void pushBurnablesAlarm() throws URISyntaxException {
    try {
      ConfirmTemplate confirmTemplate =
          new ConfirmTemplate(
              NotificationMessageType.CONFIRM.getMessage(),
              new MessageAction(YES, YES),
              new MessageAction(NO, NO));

      TemplateMessage templateMessage =
          new TemplateMessage(NotificationMessageType.BURNABLES.getMessage(), confirmTemplate);

      PushMessage pushMessage = new PushMessage(lineBotId, templateMessage);

      BotApiResponse response = lineMessagingClient.pushMessage(pushMessage).get();
      log.debug("Sent messages: {}", response);
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }
}
