package com.example.demo.brokers;

import com.example.demo.models.Tick;
import com.example.demo.receivers.Receiver;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageBroker {

    private List<Receiver> receivers =new ArrayList<>(2);

    @Async
    public void addMessage(Tick tick){
        receivers.forEach(receiver -> {
            receiver.update(tick);});
    }

    public void subscribe(Receiver receiver){
        receivers.add(receiver);
    }

    public List<Receiver> getReceivers() {
        return receivers;
    }

    public void unsubscribe(Receiver receiver){
        receivers.remove(receiver);
    }

}
