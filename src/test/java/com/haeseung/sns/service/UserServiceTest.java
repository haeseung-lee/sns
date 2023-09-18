package com.haeseung.sns.service;

import com.haeseung.sns.exception.ErrorCode;
import com.haeseung.sns.exception.SnsApplicationException;
import com.haeseung.sns.fixture.UserEntityFixture;
import com.haeseung.sns.model.entity.UserEntity;
import com.haeseung.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import scala.Option;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void 회원가입이_정상적으로_동작하는경우(){

        String userName = "userName";
        String password = "password";

        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty()); //결과가 없어야함
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(UserEntityFixture.get(userName, password));//저장된 entity 반환!
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
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));//저장된 entity 반환!

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, ()->userService.join(userName, password));
        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_NAME, e.getErrorCode());
    }

    @Test
    void 로그인이_정상적으로_동작하는경우(){

        String userName = "userName";
        String password = "password";


        UserEntity fixture = UserEntityFixture.get(userName, password);

        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);

        //성공 시 어떤 exception 도 발생하지 않음
        Assertions.assertDoesNotThrow(()->userService.login(userName, password));
    }

    @Test
    void 로그인시_userName으로_회원가입한_유저가_없는경우(){

        String userName = "userName";
        String password = "password";

        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty()); //없음

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, ()->userService.login(userName, password));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 로그인시_패스워드가_틀린_경우(){

        String userName = "userName";
        String password = "password";
        String wrongPassword = "wrongPassword";


        UserEntity fixture = UserEntityFixture.get(userName, password);

        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture)); //있음

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, ()->userService.login(userName, wrongPassword));
        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
    }
}
