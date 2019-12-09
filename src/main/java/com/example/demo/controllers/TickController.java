package com.example.demo.controllers;

import com.example.demo.brokers.MessageBroker;
import com.example.demo.models.*;
import com.example.demo.receivers.impl.TickDataReceiver;
import com.example.demo.receivers.impl.TickInstrumentsReceiver;
import com.example.demo.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class TickController {

  @Autowired private MessageBroker messageBroker;

  @Autowired private TickDataReceiver tickDataReceiver;

  @Autowired private TickInstrumentsReceiver tickInstrumentsReceiver;

  @PostMapping("/ticks")
  public ResponseEntity<?> createTicks(@Valid @RequestBody Tick tick) {
    if (TimeUtils.differenceFromCurrentTime(tick.getTimestamp()) > 60000) {
      return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    messageBroker.addMessage(tick);
    return new ResponseEntity(HttpStatus.CREATED);
  }

  @GetMapping("/statistics")
  public ResponseEntity<Statistics> getStatistics() {
    return ResponseEntity.ok().body(tickDataReceiver.getAggregatedData());
  }

  @GetMapping("/statistics/{instrument_identifier}")
  public ResponseEntity<Statistics> getStatistics(
      @PathVariable(value = "instrument_identifier") String instrument) {
    return ResponseEntity.ok().body(tickInstrumentsReceiver.getAggregatedDataByInstrument(instrument));
  }
}
