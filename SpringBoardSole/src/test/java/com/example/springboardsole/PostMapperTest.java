package com.example.springboardsole;

import com.example.springboardsole.domain.post.PostMapper;
import com.example.springboardsole.domain.post.PostRequest;
import com.example.springboardsole.domain.post.PostResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PostMapperTest {

    @Autowired
    PostMapper postMapper;

    @Test
    void save() {
        PostRequest params = new PostRequest();
        params.setTitle("1번");
        params.setContent("1번");
        params.setWriter("test");
        params.setNoticeYn(false);
        postMapper.save(params);

        List<PostResponse> posts = postMapper.findAll();

        System.out.println("전체 게시글 개수 : " + posts.size() + "개입니다");

    }
}
