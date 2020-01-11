package com.mn.linebot.garbagereminder.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.mn.linebot.garbagereminder.domain.BadMessageType;
import com.mn.linebot.garbagereminder.domain.BadStickyType;
import com.mn.linebot.garbagereminder.domain.GoodMessageType;
import com.mn.linebot.garbagereminder.domain.GoodStickyType;
import com.mn.linebot.garbagereminder.domain.Type;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class WebhookService {

  private final LineMessagingClient lineMessagingClient;

  WebhookService(LineMessagingClient lineMessagingClient) {
    this.lineMessagingClient = lineMessagingClient;
  }

  public void handleTextContent(String replyToken, Event event, TextMessageContent content)
      throws Exception {

    String text = content.getText();
    log.info("Got text message from {}: {}", replyToken, text);

    switch (text) {
      case "profile":
        {
          String userId = event.getSource().getUserId();
          if (userId != null) {
            lineMessagingClient
                .getProfile(userId)
                .whenComplete(
                    (profile, throwable) -> {
                      if (throwable != null) {
                        this.replyText(replyToken, throwable.getMessage());
                        return;
                      }

                      this.reply(
                          replyToken,
                          Arrays.asList(
                              new TextMessage("Display name: " + profile.getDisplayName()),
                              new TextMessage("Status message: " + profile.getStatusMessage()),
                              new TextMessage("userId: " + userId)));
                    });
          } else {
            this.replyText(replyToken, "Bot can't use profile API without user ID");
          }
          break;
        }
      case "bye":
        {
          Source source = event.getSource();
          if (source instanceof GroupSource) {
            this.replyText(replyToken, "Leaving group");
            lineMessagingClient.leaveGroup(((GroupSource) source).getGroupId()).get();
          } else if (source instanceof RoomSource) {
            this.replyText(replyToken, "Leaving room");
            lineMessagingClient.leaveRoom(((RoomSource) source).getRoomId()).get();
          } else {
            this.replyText(replyToken, "Bot can't leave from 1:1 chat");
          }
          break;
        }
      case "yes":
        {
          GoodStickyType stickyType = Type.of(GoodStickyType.class);
          String message = Type.of(GoodMessageType.class).getMessage();
          this.reply(
              replyToken,
              Arrays.asList(
                  new StickerMessage(stickyType.getPackageId(), stickyType.getStickerId()),
                  new TextMessage(message)));
          break;
        }
      case "no":
        {
          BadStickyType stickyType = Type.of(BadStickyType.class);
          String message = Type.of(BadMessageType.class).getMessage();
          this.reply(
              replyToken,
              Arrays.asList(
                  new StickerMessage(stickyType.getPackageId(), stickyType.getStickerId()),
                  new TextMessage(message)));
          break;
        }
      default:
        log.info("Returns echo message {}: {}", replyToken, text);
        this.replyText(replyToken, text);
        break;
    }
  }

  private void reply(@NonNull String replyToken, @NonNull Message message) {
    reply(replyToken, Collections.singletonList(message));
  }

  private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
    try {
      BotApiResponse apiResponse =
          lineMessagingClient.replyMessage(new ReplyMessage(replyToken, messages)).get();
      log.info("Sent messages: {}", apiResponse);
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  private void replyText(@NonNull String replyToken, @NonNull String message) {
    if (replyToken.isEmpty()) {
      throw new IllegalArgumentException("replyToken must not be empty");
    }
    if (message.length() > 1000) {
      message = message.substring(0, 1000 - 2) + "……";
    }
    this.reply(replyToken, new TextMessage(message));
  }
}
