package com.example.demo.receivers.impl;

import com.example.demo.models.Statistics;
import com.example.demo.models.Tick;
import com.example.demo.vo.TickAggregatedDataVO;
import com.example.demo.receivers.Receiver;
import com.example.demo.util.TimeUtils;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class TickInstrumentsReceiver implements Receiver {

    private Map<String, TickAggregatedDataVO>[] dataByInstrumentId = new HashMap[60000];

    @Override
    public void update(Tick tick) {
        int index = (int) TimeUtils.differenceFromCurrentTime(tick.getTimestamp());
        if (index > 60000) {
            return;
        }
        Map<String, TickAggregatedDataVO> aggregatedDataByInstruments =
                this.dataByInstrumentId[(index)];
        TickAggregatedDataVO tickAggregatedDataVO;
        if(aggregatedDataByInstruments==null){
            aggregatedDataByInstruments=new HashMap<>();
        }
        if ( !aggregatedDataByInstruments.containsKey(tick.getInstrument())) {
            tickAggregatedDataVO=new TickAggregatedDataVO(
                    tick.getTimestamp() / 1000, tick.getPrice(), 1, tick.getPrice(), tick.getPrice());

        } else {
            tickAggregatedDataVO = aggregatedDataByInstruments.get(tick.getInstrument());
            if (tickAggregatedDataVO==null || TimeUtils.differenceFromCurrentTime(tickAggregatedDataVO.getTimeInSeconds() * 1000)
                    > 60000) {
                tickAggregatedDataVO= new TickAggregatedDataVO(
                        tick.getTimestamp() / 1000, tick.getPrice(), 1, tick.getPrice(), tick.getPrice());

            } else {
                tickAggregatedDataVO.add(tick.getPrice());
                tickAggregatedDataVO.incrementCount();
                tickAggregatedDataVO.setMaximum(tick.getPrice()); //set has the logic of check and set
                tickAggregatedDataVO.setMinimum(tick.getPrice());
            }
        }
        aggregatedDataByInstruments.put(tick.getInstrument(),tickAggregatedDataVO);
        this.dataByInstrumentId[(index)]=aggregatedDataByInstruments;
    }

    public Statistics getAggregatedDataByInstrument(String instrument) {
        Statistics statistics = new Statistics();
        Arrays.stream(dataByInstrumentId).forEach(
                map -> {
                    if (map!=null && map.containsKey(instrument)) {
                        TickAggregatedDataVO tickAggregatedDataVO = map.get(instrument);
                        if (TimeUtils.differenceFromCurrentTime(tickAggregatedDataVO.getTimeInSeconds() * 1000)
                                < 60000) {
                            statistics.incrementCount(tickAggregatedDataVO.getCount());
                                statistics.setMaximum(tickAggregatedDataVO.getMaximum());
                                statistics.setMinimum(tickAggregatedDataVO.getMinimum());
                            statistics.setAverage(statistics.getAverage() + tickAggregatedDataVO.getSum());
                        }
                    }
                });
        statistics.setAverage(statistics.getAverage() / statistics.getCount());
        return statistics;
    }
}
