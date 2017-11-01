package com.mn.linebot.garbagereminder.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.AudioMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.VideoMessageContent;
import com.linecorp.bot.model.message.*;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import com.mn.linebot.garbagereminder.service.WebhookService;

@Slf4j
@LineMessageHandler
public class WebhookController {

    private final WebhookService webhookService;

    WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        log.info("event: {}", event);
        TextMessageContent message = event.getMessage();
        webhookService.handleTextContent(event.getReplyToken(), event, message);
    }

    @EventMapping
    public void defaultMessageEvent(Event event) {
        log.info("event: " + event);
    }

    @EventMapping
    public Message handleStickerMessage(MessageEvent<StickerMessageContent> event) {
        return new TextMessage("Thank you for sending stamp ：）");
    }

    @EventMapping
    public Message handleImageMessage(MessageEvent<ImageMessageContent> event) {
        return new TextMessage("Thank you for sending image ：D");
    }

    @EventMapping
    public Message handleVideoMessage(MessageEvent<VideoMessageContent> event) {
        return new TextMessage("Thank you for sending video XD");
    }

    @EventMapping
    public Message handleAudioMessage(MessageEvent<AudioMessageContent> event) {
        return new TextMessage("Thank you for sending audio ；）");
    }
}
