package com.cabbooking.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Handle not found errors
    @ExceptionHandler(CustomNotFoundException.class)
    public String handleNotFoundException(CustomNotFoundException ex, Model model) {
        log.error("Resource not found: {}", ex.getMessage(), ex);
        model.addAttribute("error", ex.getMessage());
        return "error"; // show error page
    }

    // Handle duplicate or DB constraint errors
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityException(DataIntegrityViolationException ex, Model model) {
        log.error("Data integrity violation: {}", ex.getMessage(), ex);
        model.addAttribute("error", "Duplicate or invalid data: " + ex.getMessage());
        return "error";
    }

    // Handle all other runtime exceptions
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, Model model) {
        log.error("Runtime exception: {}", ex.getMessage(), ex);
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    // Fallback for any exception
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        log.error("Unexpected exception: {}", ex.getMessage(), ex);
        model.addAttribute("error", "Something went wrong: " + ex.getMessage());
        return "error";
    }
}
