package com.neratama.api.common.scheduler;

import com.neratama.api.auth.token.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenCleanUpScheduler {

    private static final Logger log = LoggerFactory.getLogger(TokenCleanUpScheduler.class);
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenCleanUpScheduler(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanUpOldTokens() {
        log.info("Clean up old tokens");
        try {
            refreshTokenRepository.deleteExpiredOrUsedTokens(LocalDateTime.now());
            log.info("Old tokens cleaned up successfully");
        } catch (Exception e) {
            log.error("Error while cleaning up old tokens", e);
        }
    }
}
