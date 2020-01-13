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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/** Webhook service */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

  private static final int MAX_LENGTH = 1000;

  private static final String SHOW_PROFILE = "profile";

  private static final String LEAVE_ROOM = "bye";

  private static final String REPLY_YES = "yes";

  private static final String REPLY_NO = "no";

  private final LineMessagingClient lineMessagingClient;

  public void handleTextContent(String replyToken, Event event, TextMessageContent content)
      throws Exception {

    String text = content.getText();
    log.debug("Got text message from {}: {}", replyToken, text);

    switch (text) {
      case SHOW_PROFILE:
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

                      List<Message> messages =
                          Arrays.asList(
                              new TextMessage("Display name: " + profile.getDisplayName()),
                              new TextMessage("Status message: " + profile.getStatusMessage()),
                              new TextMessage("userId: " + userId));
                      this.reply(replyToken, messages);
                    });
          } else {
            this.replyText(replyToken, "Bot can't use profile API without user ID");
          }
          break;
        }
      case LEAVE_ROOM:
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
      case REPLY_YES:
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
      case REPLY_NO:
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
        log.debug("Returns echo message {}: {}", replyToken, text);
        this.replyText(replyToken, text);
        break;
    }
  }

  private void reply(@NonNull String replyToken, @NonNull Message message) {
    reply(replyToken, Collections.singletonList(message));
  }

  private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
    try {
      ReplyMessage replyMessage = new ReplyMessage(replyToken, messages);
      BotApiResponse apiResponse = lineMessagingClient.replyMessage(replyMessage).get();
      log.debug("Sent messages: {}", apiResponse);
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  private void replyText(@NonNull String replyToken, @NonNull String message) {
    if (replyToken.isEmpty()) {
      throw new IllegalArgumentException("replyToken must not be empty");
    }
    if (message.length() > MAX_LENGTH) {
      message = message.substring(0, MAX_LENGTH - 2) + "……";
    }
    this.reply(replyToken, new TextMessage(message));
  }
}
