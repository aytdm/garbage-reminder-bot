package com.mn.linebot.garbagereminder.controller;

import com.mn.linebot.garbagereminder.service.PushConfirmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PushConfirmController {

  private final PushConfirmService lineMessagingService;

  @GetMapping()
  public String defaultPage() {
    return "";
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
