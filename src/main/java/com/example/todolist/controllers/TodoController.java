package com.example.todolist.controllers;


import com.example.todolist.ApiResponse;
import com.example.todolist.models.ToDo;
import com.example.todolist.models.data.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse> getAllTodos() {

        try {
            List<ToDo> toDoList = todoRepository.findAll();
            return ResponseEntity.ok(new ApiResponse(true, "ToDoList retrieved.", toDoList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Error retrieving.", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getToDoById(@PathVariable int id) {
        try {
            ToDo todo = todoRepository.findById(id).orElseThrow(() -> new RuntimeException("Todo not found with id: " + id) );
            return ResponseEntity.ok(new ApiResponse(true, "ToDo retrieved successfully", todo));
        } catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Error retrieving data", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createToDo(@RequestBody ToDo toDo){
        try {
            ToDo createToDo = todoRepository.save(toDo);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true,"ToDo creates successfully",createToDo));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false,"Error creating ToDo",null));
        }
    }

    @PutMapping("/{id}")
    public  ResponseEntity<ApiResponse> updateToDo(@PathVariable int id, @RequestBody ToDo toDoDetails){
        try{
            ToDo toDo = todoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
            toDo.setTitle(toDoDetails.getTitle());
            toDo.setCompleted(toDoDetails.isCompleted());

            ToDo updatedToDo = todoRepository.save(toDo);
            return ResponseEntity.ok(new ApiResponse(true,"ToDo updated successfully",updatedToDo));
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false,"Error updating the todo with id "+id, null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTodo(@PathVariable int id) {
        try {
            ToDo toDo = todoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));

            todoRepository.delete(toDo);
            return ResponseEntity.ok(new ApiResponse(true, "ToDo deleted successfully", null));

        }catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Error deleting todo with id: " + id, null));
        }

    }
}
