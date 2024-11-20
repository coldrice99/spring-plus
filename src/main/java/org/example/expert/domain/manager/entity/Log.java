package org.example.expert.domain.manager.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;

/*
- 로그 생성 시간은 반드시 필요합니다.
- 그 외 로그에 들어가는 내용은 원하는 정보를 자유롭게 넣어주세요.
 */
@Entity
@Table(name = "log")
@NoArgsConstructor
public class Log extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String logDetails;

    @Column
    private String status;

    public Log(String logDetails, String status) {
        this.logDetails = logDetails;
        this.status = status;
    }
}
