package com.mn.linebot.garbagereminder.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Good messages */
@Getter
@RequiredArgsConstructor
public enum GoodMessageType {
  GOOD_WORK("Good work \uDBC0\uDC79"),
  AWESOME("Awesome！！\uDBC0\uDC2D\uDBC0\uDC2D"),
  KEEP("Keep working on it\uDBC0\uDC8F");

  private final String message;
}
