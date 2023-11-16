package com.example.todolist.models.data;

import com.example.todolist.models.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<ToDo,Integer> {
}
