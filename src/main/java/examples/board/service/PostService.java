package examples.board.service;

import examples.board.domain.Post;
import examples.board.dto.PostForm;

import java.util.List;

public interface PostService {
    void create(Post post, PostForm form);
    void update(Post post, PostForm form);
    void delete(Long id);
    List<Post> findAll();
    Post get(Long id);
    long count();
}
