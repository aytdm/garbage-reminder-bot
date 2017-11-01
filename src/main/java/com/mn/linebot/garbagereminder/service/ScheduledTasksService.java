package com.mn.linebot.garbagereminder.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class ScheduledTasksService {

    private final PushConfirmService lineMessagingService;
    private int i = 0;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    ScheduledTasksService(PushConfirmService lineMessagingService) {
        this.lineMessagingService = lineMessagingService;
    }

    @Scheduled(cron="${garbage.reminder.cron.burnables}", zone = "Asia/Tokyo")
    public void executeBurnablesAlarm() {
        try {
            lineMessagingService.pushBurnablesAlarm();
        } catch (URISyntaxException e) {
            log.error("{}", e);
        }
        log.info("cron executed : " + sdf.format(new Date()));
    }
}
