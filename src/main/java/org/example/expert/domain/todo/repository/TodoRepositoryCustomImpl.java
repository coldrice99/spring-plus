package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public TodoRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        // QueryDSL 에서 자동 생성된 Q 클래스를 사용해 Todo와 User 엔티티에 접근
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        Todo result = jpaQueryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin() // fetchJoin() 을 추가하여 즉시 로딩
                .where(todo.id.eq(todoId))
                .fetchOne(); // 쿼리의 결과를 하나만 가져옴 (하나일 때만 유효)

        // 조회 결과가 있을 경우 Todo 객체를 Optional로 감싸서 반환하고, 조회된 데이터가 없으면 빈 Optional이 반환
        return Optional.ofNullable(result);
    }
    QTodo todo = QTodo.todo;
    QManager manager = QManager.manager;
    QComment comment = QComment.comment;

    @Override
    public Page<TodoSearchResponse> findByKeywordAndCreatedAtBetweenAndNickname(TodoSearchRequest requestDto, Pageable pageable) {


        List<TodoSearchResponse> results = jpaQueryFactory
                .select(Projections.constructor(TodoSearchResponse.class,
                        todo.title,
                        manager.countDistinct(),
                        comment.countDistinct() // TodoSearchResponse에 넣을 데이터를 선택(할일 제목, 관리자 수, 댓글 수)
                ))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment) // todo, manager, comment를 연결해서 데이터를 가져옴
                .where(
                        titleContains(requestDto.keyword()),
                        createdAtBetween(requestDto.startDate(), requestDto.endDate()),
                        managerNicknameContains(requestDto.nickname())
                )
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc()) // 같은 todo 끼리 묶고 작성일 순서로 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()) // 페이지 처리
                .fetch();

        long totalTodo = jpaQueryFactory
                .select(todo.count())
                .from(todo)
                .fetchOne(); // 일정 총 개수

        return new PageImpl<>(results, pageable, totalTodo); // 검색된 데이터 results, 페이지 정보 pageable, 일정 개수 totalTodo를 묶어서 반환
    }

    private BooleanExpression titleContains(String keyword) {
        return keyword != null ? QTodo.todo.title.contains(keyword) : null;
    }

    private BooleanExpression createdAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate != null && endDate != null
                ? QTodo.todo.createdAt.between(startDate, endDate) : null;
    }

    private BooleanExpression managerNicknameContains(String managerNickname) {
        return managerNickname != null
                ? QManager.manager.user.nickname.contains(managerNickname) : null;
    }
}
