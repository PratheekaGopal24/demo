package com.example.demo.receivers.impl;

import com.example.demo.models.Statistics;
import com.example.demo.models.Tick;
import com.example.demo.receivers.Receiver;
import com.example.demo.util.TimeUtils;
import com.example.demo.vo.TickAggregatedDataVO;
import org.springframework.stereotype.Controller;

import java.util.Arrays;

@Controller
public class TickDataReceiver implements Receiver {

  private TickAggregatedDataVO[] tickAggregatedDataVOS = new TickAggregatedDataVO[60000];

  public Statistics getAggregatedData() {
    Statistics statistics = new Statistics();
    Arrays.stream(tickAggregatedDataVOS)
        .filter(
            tickAggregatedDataVO ->
                tickAggregatedDataVO != null
                    && TimeUtils.differenceFromCurrentTime(
                            tickAggregatedDataVO.getTimeInSeconds() * 1000)
                        < 60000)
        .forEach(
            tickAggregatedDataVO -> {
              statistics.incrementCount(tickAggregatedDataVO.getCount());
              statistics.setMaximum(tickAggregatedDataVO.getMaximum());
              statistics.setMinimum(tickAggregatedDataVO.getMinimum());
              statistics.setAverage(statistics.getAverage() + tickAggregatedDataVO.getSum());
            });

    if(statistics.getCount()!=0)
    statistics.setAverage(statistics.getAverage() / statistics.getCount());
    return statistics;
  }

  @Override
  public void update(Tick tick) {
    int index = (int) TimeUtils.differenceFromCurrentTime(tick.getTimestamp());
    if (index > 60000) {
      return;
    }

    TickAggregatedDataVO tickAggregatedDataVO = this.tickAggregatedDataVOS[(index)];

    if (tickAggregatedDataVO == null
        || TimeUtils.differenceFromCurrentTime(tickAggregatedDataVO.getTimeInSeconds() * 1000)
            > 60000) {
      tickAggregatedDataVO =
          new TickAggregatedDataVO(
              tick.getTimestamp() / 1000, tick.getPrice(), 1, tick.getPrice(), tick.getPrice());

    } else {
      tickAggregatedDataVO.add(tick.getPrice());
      tickAggregatedDataVO.incrementCount();
      tickAggregatedDataVO.setMaximum(tick.getPrice());
      tickAggregatedDataVO.setMinimum(tick.getPrice());
    }
    this.tickAggregatedDataVOS[(index)] = tickAggregatedDataVO;
  }
}
