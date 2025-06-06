package dev.HTR.exeptions;

import dev.HTR.DTOs.ErrorMessage;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handlerAlreadyExists (AlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(
                "Login is taken",
                ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> handlerBadCredentials (BadCredentialsException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorMessage(
                    "Password or Login error",
                    ex.getMessage()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorMessage> handlerExpiredJwt (ExpiredJwtException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorMessage(
                        "token expired",
                        ex.getMessage()));
    }
}
