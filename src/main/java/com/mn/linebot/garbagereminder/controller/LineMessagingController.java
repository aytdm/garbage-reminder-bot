package com.mn.linebot.garbagereminder.controller;

import com.mn.linebot.garbagereminder.domain.BadMessageType;
import com.mn.linebot.garbagereminder.domain.Type;
import com.mn.linebot.garbagereminder.service.LineMessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LineMessagingController {

  private final LineMessagingService lineMessagingService;

  @GetMapping()
  public String defaultPage() {
    return Type.of(BadMessageType.class).getMessage();
  }

  @GetMapping("api/alarm/burnables")
  public void pushBurnablesAlarm() {
    try {
      lineMessagingService.pushBurnablesAlarm();
    } catch (URISyntaxException e) {
      log.error("", e);
    }
  }
}
