package com.example.demo.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistics {
    @JsonProperty("avg")
    private double average;

    @JsonProperty("max")
    private double maximum;

    @JsonProperty("min")
    private double minimum;
    
    private long count;


    public void setMaximum(double price) {
        this.maximum = this.maximum > price ? this.maximum : price;
    }

    public void setMinimum(double price) {
        this.minimum = this.minimum>0 && this.minimum < price ? this.minimum : price;
    }

    public void incrementCount(long count){
        this.count+=count;
    }
}
