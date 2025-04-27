package com.example.rest;

import com.example.common.error.DefaultErrors;
import com.example.common.model.CalculationResponse;
import com.example.rest.service.CalculatorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@EmbeddedKafka
class CalculatorControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private CalculatorService calculatorService; // Correct annotation

    @BeforeEach
    void setUp() {
        // Create MockMvc manually
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MDC.put("requestId", UUID.randomUUID().toString());
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void testZeroDivision() throws Exception {
        String a = "5";
        String b = "0";

        String requestId = MDC.get("requestId");

        CalculationResponse mockResponse = new CalculationResponse(UUID.fromString(requestId), DefaultErrors.DIVISION_BY_ZERO);

        when(calculatorService.calculate(eq("division"), eq(a), eq(b), any(UUID.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/division")
                        .param("a", a)
                        .param("b", b)
                        .header("X-Request-ID", requestId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.requestId").value(requestId))
                .andExpect(jsonPath("$.error.code").value(DefaultErrors.DIVISION_BY_ZERO.getCode().toString()))
                .andExpect(jsonPath("$.error.message").value(DefaultErrors.DIVISION_BY_ZERO.getMessage()));

        verify(calculatorService, times(1)).calculate(eq("division"), eq(a), eq(b), any(UUID.class));
    }

    @Test
    void testSum() throws Exception {
        String a = "5";
        String b = "3";

        String requestId = MDC.get("requestId");

        CalculationResponse mockResponse = new CalculationResponse(UUID.fromString(requestId), new BigDecimal("8"));

        when(calculatorService.calculate(eq("sum"), eq(a), eq(b), any(UUID.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/sum")
                        .param("a", a)
                        .param("b", b)
                        .header("X-Request-ID", requestId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value(requestId))
                .andExpect(jsonPath("$.result").value(new BigDecimal("8")));

        verify(calculatorService, times(1)).calculate(eq("sum"), eq(a), eq(b), any(UUID.class));
    }

    @Test
    void testInvalidOperand() throws Exception {
        String a = "invalid";
        String b = "3";

        String requestId = MDC.get("requestId");

        CalculationResponse mockResponse = new CalculationResponse(UUID.fromString(requestId), DefaultErrors.INVALID_OPERAND);
        mockResponse.getError().setMessage("Invalid input for a: " + a);

        when(calculatorService.calculate(eq("sum"), eq(a), eq(b), any(UUID.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/sum")
                        .param("a", a)
                        .param("b", b)
                        .header("X-Request-ID", requestId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.requestId").value(requestId))
                .andExpect(jsonPath("$.error.code").value(DefaultErrors.INVALID_OPERAND.getCode().toString()))
                .andExpect(jsonPath("$.error.message").value("Invalid input for a: " + a));

        verify(calculatorService, times(1)).calculate(eq("sum"), eq(a), eq(b), any(UUID.class));
    }

    @Test
    void testSubtraction() throws Exception {
        String a = "5";
        String b = "3";

        String requestId = MDC.get("requestId");

        CalculationResponse mockResponse = new CalculationResponse(UUID.fromString(requestId), new BigDecimal("2"));

        when(calculatorService.calculate(eq("subtraction"), eq(a), eq(b), any(UUID.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/subtraction")
                        .param("a", a)
                        .param("b", b)
                        .header("X-Request-ID", requestId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value(requestId))
                .andExpect(jsonPath("$.result").value(new BigDecimal("2")));

        verify(calculatorService, times(1)).calculate(eq("subtraction"), eq(a), eq(b), any(UUID.class));
    }

    @Test
    void testMultiplication() throws Exception {
        String a = "5";
        String b = "3";

        String requestId = MDC.get("requestId");

        CalculationResponse mockResponse = new CalculationResponse(UUID.fromString(requestId), new BigDecimal("15"));

        when(calculatorService.calculate(eq("multiplication"), eq(a), eq(b), any(UUID.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/multiplication")
                        .param("a", a)
                        .param("b", b)
                        .header("X-Request-ID", requestId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value(requestId))
                .andExpect(jsonPath("$.result").value(new BigDecimal("15")));

        verify(calculatorService, times(1)).calculate(eq("multiplication"), eq(a), eq(b), any(UUID.class));
    }

    @Test
    void testDivision() throws Exception {
        String a = "6";
        String b = "3";

        String requestId = MDC.get("requestId");

        CalculationResponse mockResponse = new CalculationResponse(UUID.fromString(requestId), new BigDecimal("2"));

        when(calculatorService.calculate(eq("division"), eq(a), eq(b), any(UUID.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/division")
                        .param("a", a)
                        .param("b", b)
                        .header("X-Request-ID", requestId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value(requestId))
                .andExpect(jsonPath("$.result").value(new BigDecimal("2")));

        verify(calculatorService, times(1)).calculate(eq("division"), eq(a), eq(b), any(UUID.class));
    }

}
