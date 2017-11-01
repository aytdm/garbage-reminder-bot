package com.mn.linebot.garbagereminder.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
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
import org.springframework.stereotype.Service;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class WebhookService {

    private final LineMessagingClient lineMessagingClient;

    WebhookService(LineMessagingClient lineMessagingClient) {
        this.lineMessagingClient = lineMessagingClient;
    }

    public void handleTextContent(String replyToken, Event event, TextMessageContent content) throws Exception {

        String text = content.getText();
        log.info("Got text message from {}: {}", replyToken, text);

        switch (text) {
            case "profile": {
                String userId = event.getSource().getUserId();
                if (userId != null) {
                    lineMessagingClient
                            .getProfile(userId)
                            .whenComplete((profile, throwable) -> {
                                if (throwable != null) {
                                    this.replyText(replyToken, throwable.getMessage());
                                    return;
                                }

                                this.reply(
                                        replyToken,
                                        Arrays.asList(new TextMessage(
                                                        "Display name: " + profile.getDisplayName()),
                                                new TextMessage("Status message: " + profile.getStatusMessage()),
                                                new TextMessage("userId: " + userId))
                                );
                            });
                } else {
                    this.replyText(replyToken, "Bot can't use profile API without user ID");
                }
                break;
            }
            case "bye": {
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
            case "yes": {
                List<Map.Entry<String, String>> stickyList = new ArrayList<>(goodStickyList().entries());
                int index = getListIndex(stickyList.size());
                this.reply(replyToken,
                            Arrays.asList(
                                    new StickerMessage(stickyList.get(index).getKey(),
                                                        stickyList.get(index).getValue()),
                                    new TextMessage(selectReplyMessage(goodMessageList()))
                            )
                );
                break;
            }
            case "no": {
                List<Map.Entry<String, String>> stickyList = new ArrayList<>(badStickyList().entries());
                int index = getListIndex(stickyList.size());
                this.reply(replyToken,
                           Arrays.asList(
                                   new StickerMessage(stickyList.get(index).getKey(),
                                                      stickyList.get(index).getValue()),
                                   new TextMessage(selectReplyMessage(badMessageList()))
                           )
                );
                break;
            }
            default:
                log.info("Returns echo message {}: {}", replyToken, text);
                this.replyText(
                        replyToken,
                        text
                );
                break;
        }
    }

    private void reply(@NonNull String replyToken, @NonNull Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                                            .replyMessage(new ReplyMessage(replyToken, messages))
                                            .get();
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

    private String selectReplyMessage(List<String> receivedList) {
        Collections.shuffle(receivedList);
        return receivedList.get(getListIndex(receivedList.size()));
    }

    private int getListIndex(int range) {
        Random rand = new Random();
        return rand.nextInt(range);
    }

    private Multimap<String, String> goodStickyList() {

        Multimap<String,String> goodStickyList = ArrayListMultimap.create();

        goodStickyList.put("1", "4");
        goodStickyList.put("1", "5");
        goodStickyList.put("1", "13");
        goodStickyList.put("1", "14");
        goodStickyList.put("1", "106");
        goodStickyList.put("1", "114");
        goodStickyList.put("1", "125");
        goodStickyList.put("1", "134");
        goodStickyList.put("1", "137");
        goodStickyList.put("1", "138");
        goodStickyList.put("1", "407");
        goodStickyList.put("2", "40");
        goodStickyList.put("2", "41");
        goodStickyList.put("2", "144");
        goodStickyList.put("2", "164");
        goodStickyList.put("2", "172");
        goodStickyList.put("2", "516");

        return goodStickyList;
    }

    private Multimap<String, String> badStickyList() {

        Multimap<String, String> badStickyList = ArrayListMultimap.create();

        badStickyList.put("1", "9");
        badStickyList.put("1", "16");
        badStickyList.put("1", "108");
        badStickyList.put("1", "111");
        badStickyList.put("1", "113");
        badStickyList.put("1", "118");
        badStickyList.put("1", "123");
        badStickyList.put("1", "129");
        badStickyList.put("2", "18");
        badStickyList.put("2", "19");
        badStickyList.put("2", "32");
        badStickyList.put("2", "152");
        badStickyList.put("2", "166");
        badStickyList.put("2", "173");
        badStickyList.put("2", "39");
        badStickyList.put("2", "524");
        badStickyList.put("2", "527");

        return badStickyList;
    }

    private List<String> goodMessageList() {

        List<String> goodStringList = new ArrayList<>();

        goodStringList.add("Good work \uDBC0\uDC79");
        goodStringList.add("Awesome！！\uDBC0\uDC2D\uDBC0\uDC2D");
        goodStringList.add("Keep working on it\uDBC0\uDC8F");

        return goodStringList;
    }

    private List<String> badMessageList() {

        List<String> badStringList = new ArrayList<>();

        badStringList.add("Please take out the garbage\uDBC0\uDC8D！");
        badStringList.add("Please take out the garbage by tomorrow morning\uDBC0\uDC8F");
        badStringList.add("You will take out the garbage, won't you\uDBC0\uDC5F？");

        return badStringList;
    }
}
