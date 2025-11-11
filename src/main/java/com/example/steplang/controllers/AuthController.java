package com.example.steplang.controllers;

import com.example.steplang.commands.auth.LoginUserCommand;
import com.example.steplang.commands.auth.RegisterNewUserCommand;
import com.example.steplang.config.AppConfig;
import com.example.steplang.dtos.ResponseErrorDTO;
import com.example.steplang.dtos.auth.LoginUserDTO;
import com.example.steplang.dtos.auth.NewUserRegisterDTO;
import com.example.steplang.entities.RefreshToken;
import com.example.steplang.entities.User;
import com.example.steplang.errors.AuthError;
import com.example.steplang.exceptions.ApiException;
import com.example.steplang.repositories.UserRepository;
import com.example.steplang.services.RefreshTokenService;
import com.example.steplang.services.UserService;
import com.example.steplang.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth endpoints")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final RefreshTokenService refreshTokenService;
    private final AppConfig appConfig;
    @Operation(summary = "Register new user")
    @ApiResponses( {
        @ApiResponse(
            responseCode = "201",
            description = "Created",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = NewUserRegisterDTO.class),
                    examples = @ExampleObject(
                            name = "Example User",
                            value = "{ \"message\": \"User registered\", \"userId\": 1  }"
                    ))),
        @ApiResponse(
            responseCode = "409",
            description = "Conflict",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseErrorDTO.class),
                    examples = @ExampleObject(
                            name = "Email already used",
                            value = "{ \"code\": \"EMAIL_ALREADY_EXIST\", \"message\": \"Email already used\", \"status\": 409  }"
                    )))
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@Valid @RequestBody RegisterNewUserCommand command){
        User user = userService.register(command.getUsername(),command.getEmail(),command.getPassword());
        return ResponseEntity.status(201).body(new NewUserRegisterDTO("User registered",user.getId()));
    }
    @Operation(summary = "Login user")
    @ApiResponses( {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginUserDTO.class),
                            examples = @ExampleObject(
                                    name = "Successfully login",
                                    value = "{ \"refreshToken\": \"fa72bdb9-8d99-4624-8233-303f16282661\", \"accessToken\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0bWFpbDVAZ21haWwuY29tIiwiaWF0IjoxNzYyODU3NjQyLCJleHAiOjE3NjI4NTg1NDJ9.1X701QgadNdh07hXdTdfrXfiJYXVsZZa3-kJmMGooDI\"  }"
                            ))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "Invalid credentials",
                                    value = "{ \"code\": \"INVALID_CREDENTIALS\", \"message\": \"Invalid credentials\", \"status\": 400  }"
                            )))
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginUserCommand command){
        System.out.println(command.getEmail());
        User user = userRepo.findByEmail(command.getEmail())
                .orElseThrow(()->new ApiException(AuthError.USER_NOT_FOUND,400,"User not found"));

        if(!encoder.matches(command.getPassword(),user.getPassword())){
            throw new ApiException(AuthError.INVALID_CREDENTIALS, 400,"Invalid credentials");
        }

        String accessToken = jwtUtil.generateToken(command.getEmail(), appConfig.getAccessTokenSecondsTime()*1000);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return ResponseEntity.ok(new LoginUserDTO(refreshToken.getToken(), accessToken));
    }
    @Operation(summary = "Refresh accessToken")
    @ApiResponses( {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Successfully refreshed accessToken",
                                    value = "{ \"accessToken\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0bWFpbDVAZ21haWwuY29tIiwiaWF0IjoxNzYyODU3NjQyLCJleHAiOjE3NjI4NTg1NDJ9.1X701QgadNdh07hXdTdfrXfiJYXVsZZa3-kJmMGooDI\" }"
                            )))
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @Parameter(
                    in = ParameterIn.COOKIE,
                    name = "refreshToken",
                    description = "refreshToken cookie",
                    required = true
            )
            @CookieValue(name = "refreshToken") String token){

        RefreshToken refreshToken = refreshTokenService
                .verifyExpirationToken(token);

        String newAccessToken = jwtUtil.generateToken(refreshToken.getUser().getEmail(), appConfig.getAccessTokenSecondsTime()*1000);

        return ResponseEntity.ok(Map.of("accessToken",newAccessToken));
    }
    @Operation(summary = "Logout user")
    @ApiResponses( {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Successfully logged out",
                                    value = "{ \"message\": \"Logged out successfully\" }"
                            )))
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @Parameter(
                    in = ParameterIn.COOKIE,
                    name = "refreshToken",
                    description = "refreshToken cookie",
                    required = true
            )
            @CookieValue(name = "refreshToken") String token){
        RefreshToken refreshToken = refreshTokenService
                .verifyExpirationToken(token);
        refreshTokenService.deleteByTokenAndUser(refreshToken.getToken(),refreshToken.getUser());
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
