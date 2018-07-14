package com.xml.controller;


import com.xml.model.User;
import com.xml.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity login(HttpServletRequest request,
                                @RequestParam String username,
                                @RequestParam String password) {

        User user = userService.findUser(username, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("USER", user);

            return ResponseEntity.status(HttpStatus.OK).body(user.getRole());

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    @GetMapping("/log-out")
    public ModelAndView logout(HttpServletRequest request){

        HttpSession session = request.getSession();
        session.invalidate();
        return new ModelAndView("login");
    }
}
