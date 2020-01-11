package com.mn.linebot.garbagereminder.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Bad messages */
@Getter
@RequiredArgsConstructor
public enum BadMessageType {
  TAKE_OUT("Please take out the garbage\uDBC0\uDC8D！"),
  TAKE_OUT_TOMORROW("Please take out the garbage by tomorrow morning\uDBC0\uDC8F"),
  MOTIVATION("You will take out the garbage, won't you\uDBC0\uDC5F?");

  private final String message;
}
