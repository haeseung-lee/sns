package com.haeseung.sns.service;

import com.haeseung.sns.exception.SnsApplicationException;
import com.haeseung.sns.fixture.UserEntityFixture;
import com.haeseung.sns.model.entity.UserEntity;
import com.haeseung.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import scala.Option;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    private UserEntityRepository userEntityRepository;

    @Test
    void 회원가입이_정상적으로_동작하는경우(){

        String userName = "userName";
        String password = "password";

        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty()); //결과가 없어야함
        when(userEntityRepository.save(any())).thenReturn(Optional.of(UserEntityFixture.get(userName, password)));//저장된 entity 반환!
        //성공 시 어떤 exception 도 발생하지 않음
        Assertions.assertDoesNotThrow(()->userService.join(userName, password));
    }

    @Test
    void 회원가입이_userName으로_회원가입한_유저가_이미_있는경우(){

        String userName = "userName";
        String password = "password";
        UserEntity fixture = UserEntityFixture.get(userName, password);

        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture)); //있음
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));//저장된 entity 반환!

        Assertions.assertThrows(SnsApplicationException.class, ()->userService.join(userName, password));
    }

    @Test
    void 로그인이_정상적으로_동작하는경우(){

        String userName = "userName";
        String password = "password";


        UserEntity fixture = UserEntityFixture.get(userName, password);

        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        //성공 시 어떤 exception 도 발생하지 않음
        Assertions.assertDoesNotThrow(()->userService.login(userName, password));
    }

    @Test
    void 로그인시_userName으로_회원가입한_유저가_없는경우(){

        String userName = "userName";
        String password = "password";

        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty()); //없음

        Assertions.assertThrows(SnsApplicationException.class, ()->userService.join(userName, password));
    }

    @Test
    void 로그인시_패스워드가_틀린_경우(){

        String userName = "userName";
        String password = "password";
        String wrongPassword = "wrongPassword";


        UserEntity fixture = UserEntityFixture.get(userName, password);

        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture)); //있음

        Assertions.assertThrows(SnsApplicationException.class, ()->userService.join(userName, wrongPassword));
    }
}
