package com.example.demo.brokers;

import com.example.demo.models.Tick;
import com.example.demo.receivers.Receiver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
public class MessageBrokerTest {




    @Test
    public void addMessage()  {
        TestReceiver receiver1=new TestReceiver();
        TestReceiver receiver2=new TestReceiver();
        MessageBroker messageBroker=new MessageBroker();
        messageBroker.subscribe(receiver1);
        messageBroker.subscribe(receiver2);
        Assert.assertEquals(messageBroker.getReceivers().size(),2);
        messageBroker.unsubscribe(receiver1);
        Assert.assertEquals(messageBroker.getReceivers().size(),1);
        long time=new Date().getTime();
        messageBroker.addMessage(new Tick("instrument1", 122.22, time));
        Assert.assertEquals(receiver2.getTick().getInstrument(),"instrument1");
        Assert.assertEquals(receiver2.getTick().getPrice(),122.22,0);
        Assert.assertEquals(receiver2.getTick().getTimestamp(),time);
    }

    class TestReceiver implements Receiver{
        Tick tick;

        public Tick getTick(){
          return  this.tick;
        }

        @Override
        public void update(Tick tick) {
            this.tick=tick;
        }
    }
}
