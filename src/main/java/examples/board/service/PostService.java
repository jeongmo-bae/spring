package examples.board.service;

import examples.board.domain.Post;
import examples.board.dto.PostForm;

import java.util.List;
import java.util.Optional;

public interface PostService {
    void create(Post post, PostForm form);
    void update(Post post, PostForm form);
    void delete(Long id);
    List<Post> findAll();
    Optional<Post> get(Long id);
    long count();
}
