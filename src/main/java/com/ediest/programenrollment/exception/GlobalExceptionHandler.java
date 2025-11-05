package com.ediest.programenrollment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// @RestControllerAdvice  : it is special class that helps all controllers by catching and handling exceptions like any exception occured during the request sending  then badrequestmethod and showing log
// that file would be shown in log and postman shows error stack to avoid that we use this annotation it will handle it give postman or user proper format in ResponseEntity so that error properly shown at postman or frontend then user or developer can check it help to avoid from crash the project
@RestControllerAdvice
public class GlobalExceptionHandler {

    //ExceptionHandler says like whenever this specific type of exception happens , please run this method instead of showing the default error so when BadRequestexceoption exception it will print perfect message to say like handle here instead of log to postman
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleOther(Exception ex) {
//
//        return new ResponseEntity<>(Map.of("error", ex.getMessage() == null ? "internal error" : ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
//    }


}
