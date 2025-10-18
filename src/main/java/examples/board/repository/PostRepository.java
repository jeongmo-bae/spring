package examples.board.repository;

import examples.board.domain.Post;

public interface PostRepository {
    void save(Post post);
    Post findByTitle(String title);
    void deleteById(Long id);
    Iterable<Post> findAll();
    void update(Post post);
}
