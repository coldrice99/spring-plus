package org.example.expert.domain.todo.controller;

import org.example.expert.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CachingTest {

    @Autowired
    private UserService userService;

    @Test
    void testCachingPerformance() {
        String nickname = "User_b9632c0a";

        // 캐싱 전: 첫 번째 요청
        long startTime = System.currentTimeMillis();
        userService.getUserByNickname(nickname); // 캐시 미적용, DB 조회
        long dbQueryTime = System.currentTimeMillis() - startTime;
        System.out.println("캐싱 전 DB 조회 시간: " + dbQueryTime + "ms");

        // 캐싱 후: 두 번째 요청
        startTime = System.currentTimeMillis();
        userService.getUserByNickname(nickname); // 캐시 사용
        long cacheQueryTime = System.currentTimeMillis() - startTime;
        System.out.println("캐싱 후 조회 시간: " + cacheQueryTime + "ms");

        assert cacheQueryTime < dbQueryTime; // 캐싱 적용 후 시간이 줄었는지 확인
    }
}

