package com.mn.linebot.garbagereminder.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.mn.linebot.garbagereminder.domain.NotificationMessageType;
import com.mn.linebot.garbagereminder.service.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@LineMessageHandler
@RequiredArgsConstructor
public class WebhookController {

  private final WebhookService webhookService;

  @EventMapping
  public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
    log.debug("event: {}", event);
    TextMessageContent message = event.getMessage();
    webhookService.handleTextContent(event.getReplyToken(), event, message);
  }

  @EventMapping
  public void defaultMessageEvent(Event event) {
    log.debug("event: " + event);
  }

  @EventMapping
  public Message handleStickerMessage() {
    return new TextMessage(NotificationMessageType.STICKER.getMessage());
  }

  @EventMapping
  public Message handleImageMessage() {
    return new TextMessage(NotificationMessageType.IMAGE.getMessage());
  }

  @EventMapping
  public Message handleVideoMessage() {
    return new TextMessage(NotificationMessageType.VIDEO.getMessage());
  }

  @EventMapping
  public Message handleAudioMessage() {
    return new TextMessage(NotificationMessageType.AUDIO.getMessage());
  }
}
