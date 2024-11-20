package org.example.expert.domain.manager.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.manager.entity.Log;
import org.example.expert.domain.manager.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    /*
    propagation: 트랜잭션 전파 방식을 설정.
    isolation: 데이터베이스 격리 수준을 설정.
    timeout: 트랜잭션 제한 시간을 설정.
    readOnly: 읽기 전용 트랜잭션 설정.
    rollbackFor/noRollbackFor: 특정 예외 발생 시 롤백 여부를 설정.

    Propagation.REQUIRES_NEW : 로그 저장과 기존 비즈니스 로직의 트랜잭션을 독립적으로 처리하기 위해 사용
    이 메서드가 호출되면 기존 트랜잭션(만약 존재한다면)은 일시 중단되고, 새로운 트랜잭션이 시작
    메서드 실행이 완료되면 새 트랜잭션이 커밋(또는 롤백)되고, 기존 트랜잭션이 다시 진행

    기능 1 : 비즈니스 트랜잭션과 로그 트랜잭션의 독립성 유지
    기능 2 : 트랜잭션 롤백 방지
    기능 3 : 비즈니스 로직과 로그 기록 분리
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(String logDetails, String status) {
        Log log = new Log(logDetails, status);
        logRepository.save(log);
    }
}
