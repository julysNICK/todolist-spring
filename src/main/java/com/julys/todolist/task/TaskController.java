package com.julys.todolist.task;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.julys.todolist.utils.Utils;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping(value="path")
  public ResponseEntity postMethodName(@RequestBody TaskModel taskModel, HttpServletRequest request) {


      //TODO: process POST request

      var idUser = request.getAttribute("idUser");

      taskModel.setIdUser((UUID) idUser);

      var currentDateTime = LocalDateTime.now();

      if(currentDateTime.isAfter(taskModel.getEndedAt()) || currentDateTime.isAfter(taskModel.getStartAt())){
        return ResponseEntity.badRequest().body("A data de término não pode ser menor que a data atual");
      }

      if(taskModel.getStartAt().isAfter(taskModel.getEndedAt())){
        return ResponseEntity.badRequest().body("A data de início não pode ser maior que a data de término");
      }
      



      var task = this.taskRepository.save(taskModel);


      
      return ResponseEntity.ok(task);
  }


  public List<TaskModel> list(HttpServletRequest request) {
       var idUser = request.getAttribute("idUser");

        var tasks = this.taskRepository.findByIdUser((UUID) idUser);

        return tasks;


  } 


  @PutMapping("/{id}")
  public ResponseEntity update(
    @RequestBody TaskModel taskModel,
    @PathVariable UUID id,
    HttpServletRequest request
  ){

    var task = this.taskRepository.findById(id).orElse(null);

    if (task == null) {
      return ResponseEntity.badRequest().body("Tarefa não encontrada");
    }

    var idUser = request.getAttribute("idUser");

    if (task.getIdUser() != idUser) {
      return ResponseEntity.badRequest().body("Você não tem permissão para editar essa tarefa");
    }


    Utils.copyNonNullProperties(taskModel, task);

    return ResponseEntity.ok(this.taskRepository.save(task));

  }
  
}