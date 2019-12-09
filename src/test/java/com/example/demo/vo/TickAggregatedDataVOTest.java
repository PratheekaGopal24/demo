package com.example.demo.vo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TickAggregatedDataVOTest {

    @Test
    public void testAggregatedDataVO(){
        TickAggregatedDataVO vo=new TickAggregatedDataVO(1575900394,100,1,100,100);
        vo.add(200);
        vo.setMaximum(200);
        vo.setMinimum(200);
        vo.incrementCount();
        Assert.assertEquals(vo.getCount(),2);
        Assert.assertEquals(vo.getMaximum(),200,0);
        Assert.assertEquals(vo.getMinimum(),100,0);
        Assert.assertEquals(vo.getSum(),300,0);
    }
}
