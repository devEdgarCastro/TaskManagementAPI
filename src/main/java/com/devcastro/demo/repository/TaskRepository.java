package com.devcastro.demo.repository;

import com.devcastro.demo.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {
}