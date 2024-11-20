package org.example.expert.domain.todo.dto.response;

public record TodoSearchResponse(
        String title,
        Long managerCount,
        Long commentCount
) {
}
