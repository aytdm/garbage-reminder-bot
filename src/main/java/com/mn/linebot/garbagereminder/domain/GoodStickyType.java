package com.mn.linebot.garbagereminder.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Good stickies */
@Getter
@RequiredArgsConstructor
public enum GoodStickyType {
  // TODO appropriate naming
  A("1", "4"),
  B("1", "5"),
  C("1", "13"),
  D("1", "14"),
  E("1", "106"),
  F("1", "114"),
  G("1", "125"),
  H("1", "134"),
  I("1", "137"),
  J("1", "138"),
  K("1", "407"),
  L("2", "40"),
  M("2", "41"),
  N("2", "144"),
  O("2", "164"),
  P("2", "172"),
  Q("2", "516");

  private final String packageId;
  private final String stickerId;
}
