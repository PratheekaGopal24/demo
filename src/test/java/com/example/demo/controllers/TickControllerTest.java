package com.example.demo.controllers;

import com.example.demo.brokers.MessageBroker;
import com.example.demo.models.Statistics;
import com.example.demo.models.Tick;
import com.example.demo.receivers.impl.TickDataReceiver;
import com.example.demo.receivers.impl.TickInstrumentsReceiver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TickController.class)
@Import({MessageBroker.class, TickDataReceiver.class, TickInstrumentsReceiver.class})
public class TickControllerTest {

  private MockMvc mockMvc;

  @InjectMocks private TickController tickController;

  @MockBean private MessageBroker messageBroker;

  @MockBean private TickDataReceiver tickDataReceiver;

  @MockBean private TickInstrumentsReceiver tickInstrumentsReceiver;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(tickController).build();
  }

  @Test
  public void createTicks() throws Exception {
    doNothing().when(messageBroker).addMessage(any());
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/ticks")
                .content(asJsonString(new Tick("instrument1", 122.22, new Date().getTime())))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
  }

  @Test
  public void createTickswithInvalidTime() throws Exception {
    doNothing().when(messageBroker).addMessage(any());
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/ticks")
                .content(
                    asJsonString(new Tick("instrument1", 122.22, new Date().getTime() - 70000)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  public void getStatistics() throws Exception {
    when(tickDataReceiver.getAggregatedData()).thenReturn(new Statistics(100.0, 100.0, 100.0, 1));
    mockMvc
        .perform(MockMvcRequestBuilders.get("/statistics").accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.min").value(100.0))
        .andExpect(MockMvcResultMatchers.jsonPath("$.max").value(100.0))
        .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.avg").value(100.0));
  }

  @Test
  public void getInstrumentStatistics() throws Exception {
    String instrument = "Instrument1";
    when(tickInstrumentsReceiver.getAggregatedDataByInstrument(instrument))
        .thenReturn(new Statistics(100.0, 100.0, 100.0, 1));
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/statistics/{instrument}", instrument)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.min").value(100.0))
        .andExpect(MockMvcResultMatchers.jsonPath("$.max").value(100.0))
        .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.avg").value(100.0));
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
