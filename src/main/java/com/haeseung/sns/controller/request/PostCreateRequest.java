package com.haeseung.sns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateRequest {

    public PostCreateRequest(){
        super();
    }
    private String title;
    private String body;
}
