package com.example.demo.receivers.impls;

import com.example.demo.models.Statistics;
import com.example.demo.models.Tick;
import com.example.demo.receivers.impl.TickDataReceiver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
public class TickDataReceiverTest {

  @Test
  public void update() {
    long time = new Date().getTime();
    Tick tick = new Tick("instrument1", 122, time);
    Tick tick2 = new Tick("instrument2", 125, time - 300);
    Tick tick3 = new Tick("instrument2", 30, time - 300);
      Tick tick4 = new Tick("instrument2", 30, time - 70000);
    TickDataReceiver receiver = new TickDataReceiver();
    receiver.update(tick);
    receiver.update(tick2);
    receiver.update(tick3);
    receiver.update(tick4);
    Statistics statistics = receiver.getAggregatedData();
    Assert.assertEquals(3, statistics.getCount());
    Assert.assertEquals(30, statistics.getMinimum(), 0);
    Assert.assertEquals(125, statistics.getMaximum(), 0);
    Assert.assertEquals(92.33, statistics.getAverage(), 0.5);
    ;
  }
}
