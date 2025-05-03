package com.bs23.taskmanagement.repository;

import com.bs23.taskmanagement.model.Task;
import com.bs23.taskmanagement.model.TaskStatus;
import com.bs23.taskmanagement.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findAll(Specification<Task> specification, Pageable pageable);
}
