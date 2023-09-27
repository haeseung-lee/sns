package com.haeseung.sns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haeseung.sns.controller.request.PostCreateRequest;
import com.haeseung.sns.controller.request.PostModifyRequest;
import com.haeseung.sns.exception.ErrorCode;
import com.haeseung.sns.exception.SnsApplicationException;
import com.haeseung.sns.fixture.PostEntityFixture;
import com.haeseung.sns.model.Post;
import com.haeseung.sns.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;
    @Test
    @WithMockUser
    void 포스트작성() throws Exception{
        String title = "title";
        String body = "body";

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
                ).andDo(print())  //실행할 결과
                .andExpect(status().isOk()); //예상결과
    }

    @Test
    @WithAnonymousUser //어노테이션으로 표현 가능
    void 포스트작성시_로그인하지않은경우() throws Exception{
        String title = "title";
        String body = "body";

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
                ).andDo(print())  //실행할 결과
                .andExpect(status().isUnauthorized()); //예상결과
    }

    @Test
    @WithMockUser
    void 포스트수정() throws Exception{
        String title = "title";
        String body = "body";

        when(postService.modify(eq(title), eq(body), any(), any())).
                thenReturn(Post.fromEntity(PostEntityFixture.get("userName",1,1)));
        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(print())  //실행할 결과
                .andExpect(status().isOk()); //예상결과
    }

    @Test
    @WithAnonymousUser
    void 포스트수정시_로그인하지않은경우() throws Exception{
        String title = "title";
        String body = "body";

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(print())  //실행할 결과
                .andExpect(status().isUnauthorized()); //예상결과
    }

    @Test
    @WithMockUser
    void 포스트수정시_본인이_작성한_글이_아니라면_에러발생() throws Exception{
        String title = "title";
        String body = "body";

        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(title), eq(body), any(), eq(1));
        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(print())  //실행할 결과
                .andExpect(status().isUnauthorized()); //예상결과
    }

    @Test
    @WithMockUser
    void 포스트수정시_수정하려는_글이_없는경우_에러발생() throws Exception{
        String title = "title";
        String body = "body";

        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(eq(title), eq(body), any(), eq(1));

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(print())  //실행할 결과
                .andExpect(status().isNotFound()); //예상결과
    }
}
