package com.example.demo.receivers;

import com.example.demo.models.Tick;

@FunctionalInterface
public interface Receiver {
  public void update(Tick tick);
}
