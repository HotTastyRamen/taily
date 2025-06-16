package dev.HTR.exeptions;

import dev.HTR.DTOs.ErrorMessage;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorMessage> handlerAlreadyExists (ExpiredJwtException ex){
        System.out.println("ExpiredJwtException");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessage(

                "Токен доступа просрочен. Пройдите повторную аутентификацию",
                ex.getMessage()));
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorMessage> handlerAlreadyExists (SignatureException ex){
        System.out.println("SignatureException");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessage(
                "Подпись токена доступа не валидна!",
                ex.getMessage()));
    }

}
