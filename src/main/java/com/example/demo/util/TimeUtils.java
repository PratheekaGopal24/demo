package com.example.demo.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeUtils {

  public static final long TIME_WINDOW_IN_MILLI_SECONDS=60000;

  private TimeUtils(){
    throw new AssertionError("Cannot instantiate!!");
  }

  public static long differenceFromCurrentTime(long time) {
    Instant now = Instant.now();
    return ChronoUnit.MILLIS.between(new Timestamp(time).toInstant(), now);
  }

  public static boolean isWithinTimeRange(long timeInMilliSeconds,long range){
    return TimeUtils.differenceFromCurrentTime(timeInMilliSeconds) <= range;
  }
}


