package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    // 1. weather 조건만 있는 경우
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user WHERE t.weather =:weather ORDER BY t.modifiedAt DESC")
    Page<Todo> findByWeather(@Param("weather") String weather, Pageable pageable);

    // 2. 수정일 기간 조건만 있는 경우
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user WHERE t.modifiedAt BETWEEN :start AND :end ORDER BY t.modifiedAt DESC")
    Page<Todo> findByModifiedAtBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable);

    // 3. weather 와 수정일 기간 조건 모두 있는 경우
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user WHERE t.weather=:weather AND t.modifiedAt BETWEEN :start AND :end ORDER BY t.modifiedAt DESC")
    Page<Todo> findByWeatherAndModifiedAtBetween(
            @Param("weather") String weather,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);
}
