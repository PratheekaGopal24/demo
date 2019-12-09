package com.example.demo.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeUtils {

  public static long differenceFromCurrentTime(long time) {
    Instant now = Instant.now();
    return ChronoUnit.MILLIS.between(new Timestamp(time).toInstant(), now);
  }
}
