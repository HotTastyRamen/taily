package dev.HTR.controllers;

import dev.HTR.DTOs.VerJWT;
import dev.HTR.DTOs.VerificationCode;
import dev.HTR.services.CacheService;
import dev.HTR.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@AllArgsConstructor
public class VerificationController {

    @Autowired
    private final CacheService cacheService;

    @Autowired
    private final JwtUtils jwtUtils;

    @PostMapping("/getVerToken")
    public ResponseEntity<?> getVerToken (@RequestBody VerificationCode verificationCode){

        if (verificationCode.getCode().equals(cacheService.getCache(verificationCode.getUserId()))){
            return ResponseEntity.ok(new VerJWT(
                    jwtUtils.generateTokenWithUserId(verificationCode.getUserId())));
        };

        return new ResponseEntity<String>(
                "Unauthorized. Issue with resolving given code",
                HttpStatus.UNAUTHORIZED);
    }

}
