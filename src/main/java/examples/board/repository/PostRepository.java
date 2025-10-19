package examples.board.repository;

import examples.board.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    void save(Post post);
    Optional<Post> findById(Long id);
    List<Post> findAll();
    void update(Post post);
    void delete(Long id);
    int count();
}
