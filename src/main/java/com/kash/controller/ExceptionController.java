package com.kash.controller;

import com.kash.dto.Response;
import com.kash.exception.ExcelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionController extends ResponseEntityExceptionHandler implements RequestBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return false;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return null;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return null;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return null;
    }


    /*----Handle The Custom HttpMessageNotReadable Exception----*/
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("===: ExceptionController:: Inside handleHttpMessageNotReadable Method :===");
        return ResponseEntity.ok().body(getResponse("HMNR - " + ex.getLocalizedMessage(), "400"));

    }


    /*----Handle The Custom MethodArgumentNotValid Exception.----*/
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("===: ExceptionController:: Inside handleMethodArgumentNotValid Method :===");
        // Here we are getting all the error which is invalid as per our java bean Validation
        List<String> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .toList();
        // returning 400 Bad Request in HTTP STATUS and all the invalid parameter details in body
        return ResponseEntity.ok().body(getResponse(String.valueOf(validationErrors), "400"));

    }


    /*----Handle The Custom ServletRequestBinding Exception.----*/
    protected ResponseEntity<Object> handleServletRequestBindingException (ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("===: ExceptionController:: Inside handleServletRequestBindingException Method :===");
        return ResponseEntity.ok().body(getResponse("SRBE - " + ex.getLocalizedMessage(), "400"));
    }

    /*----Handle The Custom ConstraintViolationException Exception.----*/
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException (Exception ex) {

        log.info("===: ExceptionController:: Inside handleDataIntegrityViolationException Method :===");
        String message = ex.getMessage();
        return new ResponseEntity<>(getResponse(message, "EXCEL_500"), HttpStatus.CONFLICT);
    }

    /*----Handle The Custom RuntimeException Exception.----*/
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Object> runtimeException (RuntimeException ex) {

        log.info("===: ExceptionController:: Inside runtimeException Method :===");
        String message = ex.getMessage();
        return new ResponseEntity<>(getResponse(message, "EXCEL_500"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*----Handle The Custom QPickerException Exception.----*/
    @ExceptionHandler(value = ExcelException.class)
    public ResponseEntity<Object> generalException (ExcelException gExcp) {
        log.info("===: ExceptionController:: Inside generalException Method :===");
        String message = gExcp.getMessage();
        return new ResponseEntity<>(getResponse(message, "EXCEL_400"), HttpStatus.BAD_REQUEST);
    }


    /*----Handle The Custom SQLException Exception.----*/
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> handleSQLException (final SQLException ex) {
        log.info("===: ExceptionController:: Inside handleSQLException Method :===");
        return ResponseEntity.ok().body(getResponse(ex.getMessage(), "SQL"));
    }

    /*----Handle The Custom IllegalArgumentException Exception.----*/
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException (final IllegalArgumentException ex) {
        log.info("===: ExceptionController:: Inside handleIllegalArgumentException Method :===");
        return ResponseEntity.ok().body(getResponse(ex.getMessage(), "IAE"));
    }

    /*----Handle The Custom IOException Exception.----*/
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleIOException (final IOException ex) {
        log.info("===: ExceptionController:: Inside handleIOException Method :===");
        return ResponseEntity.ok().body(getResponse(ex.getMessage(), "IOE"));
    }

    /*----Handle The Custom Exception.----*/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException (final Exception ex) {
        log.info("===: ExceptionController:: Inside handleAllException Method :===");
        ex.printStackTrace();
        return ResponseEntity.ok().body(getResponse(ex.getMessage(), "500"));
    }


    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("===: ExceptionController:: Inside handleServletRequestBindingException Method :===");
        return ResponseEntity.ok().body(getResponse("SRBE - " + ex.getLocalizedMessage(), "400"));

    }



    private Response getResponse (String message, String code) {
        Response response = new Response();
        response.setStatus("FAILURE");
        response.setErrorMessage(message);
        response.setStatusCode(code);
        response.setResponseType("E");
        return response;
    }
}
