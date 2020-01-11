package com.mn.linebot.garbagereminder.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.AudioMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.VideoMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.mn.linebot.garbagereminder.domain.NotificationMessageType;
import com.mn.linebot.garbagereminder.service.WebhookService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@LineMessageHandler
public class WebhookController {

  private final WebhookService webhookService;

  WebhookController(WebhookService webhookService) {
    this.webhookService = webhookService;
  }

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
  public Message handleStickerMessage(MessageEvent<StickerMessageContent> event) {
    return new TextMessage(NotificationMessageType.STICKER.getMessage());
  }

  @EventMapping
  public Message handleImageMessage(MessageEvent<ImageMessageContent> event) {
    return new TextMessage(NotificationMessageType.IMAGE.getMessage());
  }

  @EventMapping
  public Message handleVideoMessage(MessageEvent<VideoMessageContent> event) {
    return new TextMessage(NotificationMessageType.VIDEO.getMessage());
  }

  @EventMapping
  public Message handleAudioMessage(MessageEvent<AudioMessageContent> event) {
    return new TextMessage(NotificationMessageType.AUDIO.getMessage());
  }
}
