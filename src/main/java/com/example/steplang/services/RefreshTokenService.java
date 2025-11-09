package com.example.steplang.services;

import com.example.steplang.config.AppConfig;
import com.example.steplang.entities.RefreshToken;
import com.example.steplang.entities.User;
import com.example.steplang.repositories.RefreshTokenRepository;
import com.example.steplang.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    public final RefreshTokenRepository refreshTokenRepo;
    private final UserRepository userRepo;
    private final AppConfig appConfig;

    @Transactional
    public RefreshToken createRefreshToken(User user){
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpirationDate(Instant.now().plusSeconds(appConfig.getRefreshTokenSecondsTime()));
        return refreshTokenRepo.save(token);
    }
    @Transactional
    public RefreshToken verifyExpirationToken(String token){
        RefreshToken refreshToken = refreshTokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refreshToken"));

        if(refreshToken.getExpirationDate().isBefore(Instant.now())){
            refreshTokenRepo.delete(refreshToken);
            throw new RuntimeException("RefreshToken expired. Please login again.");
        }
        return refreshToken;
    }

    @Transactional
    public long deleteByUser(User user){
        long deletedTokensCount = refreshTokenRepo.deleteByUser(user);
        return deletedTokensCount;
    }
    @Transactional
    public void deleteByTokenAndUser(String token,User user){
        refreshTokenRepo.deleteByTokenAndUser(token,user);
    }


    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // cleaning old refreshTokens every day at midnight
    public void deleteExpiredTokens(){
        Instant now = Instant.now();
        int before = refreshTokenRepo.findAll().size();
        refreshTokenRepo.deleteByExpirationDateBefore(now);
        int after = refreshTokenRepo.findAll().size();
    }
}
