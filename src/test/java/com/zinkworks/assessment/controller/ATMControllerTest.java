package com.zinkworks.assessment.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.zinkworks.assessment.model.Atm;
import com.zinkworks.assessment.repository.AtmRepository;

@WebMvcTest(controllers = {ATMController.class})
class ATMControllerTest {
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private AtmRepository atmRepository;
  
  @Test
  void getATMTransactions_shouldReturnATMTransactions() throws Exception {
    Atm atm = new Atm();
    when(atmRepository.findAllById(1L)).thenReturn(atm);
    mockMvc.perform(get("/atm/transactions")).andExpect(jsonPath("$.balance").value(1500))
    .andExpect(jsonPath("$.statements").isEmpty());
  }
}
