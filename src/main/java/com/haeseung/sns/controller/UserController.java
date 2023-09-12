package com.haeseung.sns.controller;

import com.haeseung.sns.controller.request.UserJoinRequest;
import com.haeseung.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //TODO : implemnts
    @PostMapping("/join")
    public void join(@RequestBody UserJoinRequest request){

        //join
        userService.join(request.getUserName(),request.getPassword());
    }
}
