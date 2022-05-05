package com.mn.linebot.garbagereminder.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.AudioMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.VideoMessageContent;
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
    webhookService.handleTextContent(event);
  }

  @EventMapping
  public void defaultMessageEvent(Event event) {
    log.debug("event: " + event);
  }

  @EventMapping
  public void handleStickerMessage(MessageEvent<StickerMessageContent> event) {
    webhookService.reply(
        event.getReplyToken(), new TextMessage(NotificationMessageType.STICKER.getMessage()));
  }

  @EventMapping
  public void handleImageMessage(MessageEvent<ImageMessageContent> event) {
    webhookService.reply(
        event.getReplyToken(), new TextMessage(NotificationMessageType.IMAGE.getMessage()));
  }

  @EventMapping
  public void handleVideoMessage(MessageEvent<VideoMessageContent> event) {
    webhookService.reply(
        event.getReplyToken(), new TextMessage(NotificationMessageType.VIDEO.getMessage()));
  }

  @EventMapping
  public void handleAudioMessage(MessageEvent<AudioMessageContent> event) {
    webhookService.reply(
        event.getReplyToken(), new TextMessage(NotificationMessageType.AUDIO.getMessage()));
  }
}
