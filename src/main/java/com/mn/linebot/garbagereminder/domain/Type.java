package com.mn.linebot.garbagereminder.domain;

import java.util.Random;

public class Type {
  /** Randomly select value */
  public static <E extends Enum> E of(Class<E> target) {
    E[] types = target.getEnumConstants();
    int index = new Random().nextInt(types.length);
    return types[index];
  }
}
