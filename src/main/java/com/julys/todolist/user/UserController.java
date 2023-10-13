package com.julys.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private IUserRepository userRepository;

  @PostMapping("/")
  public ResponseEntity<?> createUser(@RequestBody UserModel user) {

    var userExists = userRepository.findByUsername(user.getName());
    if (userExists != null) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
    }

    BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());

    var createdUser = userRepository.save(user);

    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }
}