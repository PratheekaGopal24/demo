package com.example.demo;

import com.example.demo.brokers.MessageBroker;
import com.example.demo.receivers.impl.TickDataReceiver;
import com.example.demo.receivers.impl.TickInstrumentsReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

  @Autowired private MessageBroker messageBroker;

  @Autowired private TickDataReceiver tickDataReceiver;

  @Autowired private TickInstrumentsReceiver tickInstrumentsReceiver;

  @Override
  public void run(String... args) throws Exception {
    messageBroker.subscribe(tickDataReceiver);
    messageBroker.subscribe(tickInstrumentsReceiver);
  }
}
