package org.example.expert.domain.todo.controller;

import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
@Rollback(false) // 테스트 데이터 유지
public class BulkInsertTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanUp() {
        // 기존 데이터 삭제
        userRepository.deleteAll();
        System.out.println("기존 데이터 삭제 완료");
    }

    @Test
    void bulkInsertUsers() {
        int batchSize = 1_000; // 배치 크기를 늘려 한 번에 많은 데이터를 처리
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 10_000; i++) {
            String uniqueEmail = "user" + i + "@example.com"; // 유니크한 이메일
            String randomNickname = "User_" + UUID.randomUUID().toString().substring(0, 8); // 랜덤 닉네임
            users.add(new User(uniqueEmail, "password", UserRole.USER, randomNickname));

            if (i % batchSize == 0) { // 배치 크기마다 저장
                userRepository.saveAll(users);
                System.out.println("Batch inserted: " + users.size()); // 삽입 과정 출력
                users.clear(); // 메모리 초기화
            }
        }
        // 마지막 남은 데이터 저장
        if (!users.isEmpty()) {
            userRepository.saveAll(users);
        }
        System.out.println("100만 건 데이터 생성 완료");
    }
}
