package com.example.calculator;

import com.example.calculator.service.CalculationService;
import com.example.common.model.CalculationRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka
class CalculatorServiceTests {

    private static CalculationService calculatorService;

    @BeforeAll
    static void setUp() {
         calculatorService = new CalculationService();
    }


    @Test
    void validSumTest() {

        String operation = "sum";
        BigDecimal a = new BigDecimal(5);
        BigDecimal b = new BigDecimal(3);
        CalculationRequest request = new CalculationRequest();
        request.setOperation(operation);
        request.setA(a);
        request.setB(b);

        BigDecimal result = calculatorService.calculate(request);
        assertNotNull(result);
        assertEquals(new BigDecimal(8), result);

    }

    @Test
    void validSubtractionTest() {

        String operation = "subtraction";
        BigDecimal a = new BigDecimal(5);
        BigDecimal b = new BigDecimal(3);
        CalculationRequest request = new CalculationRequest();
        request.setOperation(operation);
        request.setA(a);
        request.setB(b);

        BigDecimal result = calculatorService.calculate(request);
        assertNotNull(result);
        assertEquals(new BigDecimal(2), result);

    }

    @Test
    void validMultiplicationTest() {

        String operation = "multiplication";
        BigDecimal a = new BigDecimal(5);
        BigDecimal b = new BigDecimal(3);
        CalculationRequest request = new CalculationRequest();
        request.setOperation(operation);
        request.setA(a);
        request.setB(b);

        BigDecimal result = calculatorService.calculate(request);
        assertNotNull(result);
        assertEquals(new BigDecimal(15), result);

    }

    @Test
    void validDivisionTest() {

        String operation = "division";
        BigDecimal a = new BigDecimal(10);
        BigDecimal b = new BigDecimal(4);
        CalculationRequest request = new CalculationRequest();
        request.setOperation(operation);
        request.setA(a);
        request.setB(b);

        BigDecimal result = calculatorService.calculate(request);
        assertNotNull(result);
        assertEquals(new BigDecimal("2.5").setScale(10, RoundingMode.HALF_UP), result);

    }

    @Test
    void zeroDivisionTest() {

        String operation = "division";
        BigDecimal a = new BigDecimal(5);
        BigDecimal b = new BigDecimal(0);
        CalculationRequest request = new CalculationRequest();
        request.setOperation(operation);
        request.setA(a);
        request.setB(b);

        assertThrowsExactly(ArithmeticException.class, () -> calculatorService.calculate(request));

    }

    @Test
    void invalidOperationTest() {

        String operation = "invalid";
        BigDecimal a = new BigDecimal(5);
        BigDecimal b = new BigDecimal(3);
        CalculationRequest request = new CalculationRequest();
        request.setOperation(operation);
        request.setA(a);
        request.setB(b);

        assertThrowsExactly(IllegalArgumentException.class, () -> calculatorService.calculate(request));

    }


}
