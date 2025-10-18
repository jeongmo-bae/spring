package examples.board.repository;

import examples.board.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplatePostRepositoryImpl implements PostRepository{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplatePostRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Post post) {

    }

    @Override
    public Post findByTitle(String title) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Iterable<Post> findAll() {
        return null;
    }

    @Override
    public void update(Post post) {

    }
}
