package com.haeseung.sns.service;

import com.haeseung.sns.exception.ErrorCode;
import com.haeseung.sns.exception.SnsApplicationException;
import com.haeseung.sns.model.entity.PostEntity;
import com.haeseung.sns.model.entity.UserEntity;
import com.haeseung.sns.repository.PostEntityRepository;
import com.haeseung.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    @Transactional
    public void create(String title, String body, String userName){
        //user find
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
        //post save
        postEntityRepository.save(PostEntity.of(title, body, userEntity));
    }

    @Transactional
    public void modify(String title, String body, String userName, Integer postId){
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
        // post exist

        // post permission
    }
}
