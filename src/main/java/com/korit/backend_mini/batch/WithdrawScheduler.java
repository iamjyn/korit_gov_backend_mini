package com.korit.backend_mini.batch;

import com.korit.backend_mini.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WithdrawScheduler {

    @Autowired
    private UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredUser() {
        System.out.println("스케쥴러 작동");
        userRepository.deleteUser();
    }
}
