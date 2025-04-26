package com.example.common.error;

public class DefaultErrors {

    public static final Error INVALID_OPERAND = new Error(
            ErrorCode.INVALID_OPERAND,
            "Operand must be a number");

    public static final Error DIVISION_BY_ZERO = new Error(
            ErrorCode.DIVISION_BY_ZERO,
            "Division by zero is not allowed");

    public static final Error INTERNAL_SERVER_ERROR = new Error(
            ErrorCode.INTERNAL_SERVER_ERROR,
            "An internal server error occurred");
}
