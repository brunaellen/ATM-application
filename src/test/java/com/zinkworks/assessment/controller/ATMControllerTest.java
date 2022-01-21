package com.zinkworks.assessment.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.zinkworks.assessment.model.Atm;
import com.zinkworks.assessment.service.ATMService;

@WebMvcTest(controllers = {ATMController.class})
class ATMControllerTest {
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private ATMService atmService;
  
  @Test
  void getATMTransactions_shouldReturnATMTransactions() throws Exception {
    when(atmService.getAtm(1L)).thenReturn(new Atm(1L, BigDecimal.valueOf(1500D), 10, 30, 30, 20));

    atmService.getAtm(1L);
    mockMvc.perform(get("/atm/transactions"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.balance").value(1500.00))
      .andExpect(jsonPath("$.statements").isEmpty());
  }
}
