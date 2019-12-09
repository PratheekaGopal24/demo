package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TickAggregatedDataVO {

  private long timeInSeconds;
  private double sum;
  private long count;
  private double maximum=Double.MIN_VALUE;
  private double minimum=Double.MAX_VALUE;

  public void add(double price) {
    this.sum += price;
  }

  public void setMaximum(double price) {
    this.maximum = this.maximum > price ? this.maximum : price;
  }

  public void setMinimum(double price) {
    this.minimum = this.minimum < price ? this.minimum : price;
  }

  public void incrementCount() {
    this.count++;
  }
}
