package org.example.expert.domain.todo.dto.request;

import java.time.LocalDateTime;

public record TodoSearchRequest(
        String keyword,
        String nickname,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}