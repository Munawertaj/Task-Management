package com.bs23.taskmanagement.repository;

import com.bs23.taskmanagement.model.Task;
import com.bs23.taskmanagement.model.TaskStatus;
import com.bs23.taskmanagement.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByUser(User user, Pageable pageable);

    Page<Task> findByUserAndStatus(User user, TaskStatus status, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.title LIKE %:search%")
    Page<Task> findByUserAndTitleContaining(@Param("user") User user, @Param("search") String search, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.status = :status AND t.title LIKE %:search%")
    Page<Task> findByUserAndStatusAndTitleContaining(
            @Param("user") User user,
            @Param("status") TaskStatus status,
            @Param("search") String search,
            Pageable pageable);
}
