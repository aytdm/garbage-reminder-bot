package com.mn.linebot.garbagereminder.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Notification messages */
@Getter
@RequiredArgsConstructor
public enum NotificationMessageType {
  STICKER("Thank you for sending stamp ：）"),
  IMAGE("Thank you for sending image ：D"),
  VIDEO("Thank you for sending video XD"),
  AUDIO("Thank you for sending audio ；）"),
  BURNABLES("Tomorrow is the garbage day for burnables！"),
  CONFIRM("Did you take out the garbage?");

  private final String message;
}
