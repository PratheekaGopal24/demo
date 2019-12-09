package com.example.demo.receivers.impls;

import com.example.demo.models.Statistics;
import com.example.demo.models.Tick;
import com.example.demo.receivers.impl.TickDataReceiver;
import com.example.demo.receivers.impl.TickInstrumentsReceiver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
public class TickInstrumentReceiverTest {

    @Test
    public void update() {
        long time = new Date().getTime();
        Tick tick = new Tick("instrument1", 122, time);
        Tick tick2 = new Tick("instrument2", 125, time - 300);
        Tick tick3 = new Tick("instrument2", 30, time - 300);
        Tick tick4 = new Tick("instrument2", 30, time - 70000);
        TickInstrumentsReceiver receiver = new TickInstrumentsReceiver();
        receiver.update(tick);
        receiver.update(tick2);
        receiver.update(tick3);
        receiver.update(tick4);
        Statistics statistics = receiver.getAggregatedDataByInstrument("instrument2");
        Assert.assertEquals(2, statistics.getCount());
        Assert.assertEquals(30, statistics.getMinimum(), 0);
        Assert.assertEquals(125, statistics.getMaximum(), 0);
        Assert.assertEquals(77.5, statistics.getAverage(), 0.5);
        ;
    }
}
