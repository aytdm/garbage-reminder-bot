package com.mn.linebot.garbagereminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GarbageReminderApplication {

  public static void main(String[] args) {
    SpringApplication.run(GarbageReminderApplication.class, args);
  }
}
