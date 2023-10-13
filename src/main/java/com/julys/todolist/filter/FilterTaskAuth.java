package com.julys.todolist.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.julys.todolist.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter{

@Autowired
private IUserRepository userRepository;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // TODO Auto-generated method stub
    
   var servletPath = request.getServletPath();

   if (servletPath.startsWith("/tasks/")){
     var authorization = request.getHeader("Authorization");

   String user_password = authorization.substring("Basic".length()).trim();

    String user_password_decoded = new String(java.util.Base64.getDecoder().decode(user_password));

    String[] user_password_array = user_password_decoded.split(":");

    String username = user_password_array[0];

    String password = user_password_array[1];

    var user = userRepository.findByUsername(username);

    if (user == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    var verifyPassword = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

    if (!verifyPassword.verified) {

      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }
    request.setAttribute("idUser", user.getId());
    filterChain.doFilter(request, response);

  } else {
    filterChain.doFilter(request, response);
   }
  }

 

}