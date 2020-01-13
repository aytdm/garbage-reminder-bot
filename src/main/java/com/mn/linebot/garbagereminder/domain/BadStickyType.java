package com.mn.linebot.garbagereminder.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Bad stickies */
@Getter
@RequiredArgsConstructor
public enum BadStickyType {
  // TODO appropriate naming
  A("1", "9"),
  B("1", "16"),
  C("1", "108"),
  D("1", "111"),
  E("1", "113"),
  F("1", "118"),
  G("1", "123"),
  H("1", "129"),
  I("2", "18"),
  J("2", "19"),
  K("2", "32"),
  L("2", "152"),
  M("2", "166"),
  N("2", "173"),
  O("2", "39"),
  Q("2", "524"),
  R("2", "527");

  private final String packageId;
  private final String stickerId;
}
