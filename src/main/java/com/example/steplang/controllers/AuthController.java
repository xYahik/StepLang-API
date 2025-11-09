package com.example.steplang.controllers;

import com.example.steplang.config.AppConfig;
import com.example.steplang.entities.RefreshToken;
import com.example.steplang.entities.User;
import com.example.steplang.repositories.UserRepository;
import com.example.steplang.services.RefreshTokenService;
import com.example.steplang.services.UserService;
import com.example.steplang.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final RefreshTokenService refreshTokenService;
    private final AppConfig appConfig;

    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@RequestBody Map<String,String> req){
        String username = req.get("username");
        String email = req.get("email");
        String password = req.get("password");

        User user = userService.register(username,email,password);
        return ResponseEntity.ok(Map.of("message","User registered","id",user.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String,String> req){
        String email = req.get("email");
        String password = req.get("password");

        User user = userRepo.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));

        if(!encoder.matches(password,user.getPassword())){
            return ResponseEntity.badRequest().body(Map.of("error","Invalid credentials"));
        }

        String accessToken = jwtUtil.generateToken(email, appConfig.getAccessTokenSecondsTime()*1000);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return ResponseEntity.ok(Map.of(
                "accessToken",accessToken,
                "refreshToken", refreshToken.getToken()
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name = "refreshToken") String token){

        RefreshToken refreshToken = refreshTokenService
                .verifyExpirationToken(token);

        String newAccessToken = jwtUtil.generateToken(refreshToken.getUser().getEmail(), appConfig.getAccessTokenSecondsTime()*1000);

        return ResponseEntity.ok(Map.of("accessToken",newAccessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "refreshToken") String token){
        RefreshToken refreshToken = refreshTokenService
                .verifyExpirationToken(token);
        refreshTokenService.deleteByTokenAndUser(refreshToken.getToken(),refreshToken.getUser());
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
