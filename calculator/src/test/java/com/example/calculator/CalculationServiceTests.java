package com.example.calculator;

import com.example.calculator.service.CalculationService;
import com.example.common.model.CalculationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka
class CalculationServiceTests {

    private static CalculationService calculatorService;
    private static CalculationRequest request;

    @BeforeEach
    void setUp() {
         calculatorService = new CalculationService();
         request = new CalculationRequest();
         request.setA(new BigDecimal(5));
         request.setB(new BigDecimal(3));
    }

    @Test
    void validSumTest() {

        request.setOperation("sum");
        BigDecimal result = calculatorService.calculate(request);
        assertNotNull(result);
        assertEquals(new BigDecimal(8), result);

    }

    @Test
    void validSubtractionTest() {

        request.setOperation("subtraction");
        BigDecimal result = calculatorService.calculate(request);
        assertNotNull(result);
        assertEquals(new BigDecimal(2), result);

    }

    @Test
    void validMultiplicationTest() {

        request.setOperation("multiplication");
        BigDecimal result = calculatorService.calculate(request);
        assertNotNull(result);
        assertEquals(new BigDecimal(15), result);

    }

    @Test
    void validDivisionTest() {

        request.setOperation("division");
        BigDecimal result = calculatorService.calculate(request);
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(5.0 / 3).setScale(10, RoundingMode.HALF_UP), result);

    }

    @Test
    void zeroDivisionTest() {

        request.setOperation("division");
        request.setB(BigDecimal.ZERO);
        assertThrowsExactly(ArithmeticException.class, () -> calculatorService.calculate(request));

    }

    @Test
    void invalidOperationTest() {

        request.setOperation("invalid");

        assertThrowsExactly(IllegalArgumentException.class, () -> calculatorService.calculate(request));

    }


}
