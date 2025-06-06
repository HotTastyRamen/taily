package dev.HTR.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode {

    public VerificationCode(Long code){
        this.code = code;
        this.userId = null;
    }

    Long userId;
    Long code;

}
