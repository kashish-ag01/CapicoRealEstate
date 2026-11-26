package com.capico.user.service;

import com.capico.user.entity.RevokedToken;
import com.capico.user.repository.RevokedTokenRepository;
import com.capico.user.security.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@Transactional
public class LogoutService {

    private final RevokedTokenRepository revokedTokenRepository;
    private final JwtService jwtService;
    private final UserActivityService userActivityService;

    public LogoutService(
            RevokedTokenRepository revokedTokenRepository,
            JwtService jwtService,
            UserActivityService userActivityService
    ) {
        this.revokedTokenRepository = revokedTokenRepository;
        this.jwtService = jwtService;
        this.userActivityService = userActivityService;
    }

    public void logout(
            String token,
            String ipAddress
    ) {
        String tokenId = jwtService.extractTokenId(token);
        Long userId = jwtService.extractUserId(token);
        String email = jwtService.extractEmail(token);

        if (revokedTokenRepository.existsByTokenId(tokenId)) {
            return;
        }

        LocalDateTime expiryDate =
                jwtService.extractExpiration(token)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

        RevokedToken revokedToken = new RevokedToken();

        revokedToken.setTokenId(tokenId);
        revokedToken.setUserId(userId);
        revokedToken.setRevokedDate(LocalDateTime.now());
        revokedToken.setExpiryDate(expiryDate);

        revokedTokenRepository.save(revokedToken);

        userActivityService.recordLogout(
                userId,
                email,
                ipAddress
        );
    }
}