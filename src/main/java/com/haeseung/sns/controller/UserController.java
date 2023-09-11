package com.haeseung.sns.controller;

import com.haeseung.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //TODO : implemnts
    @PostMapping
    public void join(){

        //join
        userService.join("","");
    }
}
