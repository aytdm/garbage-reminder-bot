package com.mn.linebot.garbagereminder.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class ScheduledTasksService {
  public static final String TZ = "Asia/Tokyo";

  private static final transient DateTimeFormatter DATE_TIME_FORMAT =
      DateTimeFormatter.ofPattern("HH:mm:ss");

  private final PushConfirmService lineMessagingService;

  ScheduledTasksService(PushConfirmService lineMessagingService) {
    this.lineMessagingService = lineMessagingService;
  }

  @Scheduled(cron = "${garbage.reminder.cron.burnables}", zone = "Asia/Tokyo")
  public void executeBurnablesAlarm() {
    try {
      lineMessagingService.pushBurnablesAlarm();
    } catch (URISyntaxException e) {
      log.error("{}", e);
    }
    log.debug("cron executed : " + DATE_TIME_FORMAT.format(LocalDate.now(ZoneId.of(TZ))));
  }
}
