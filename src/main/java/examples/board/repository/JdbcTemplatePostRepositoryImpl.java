package examples.board.repository;

import examples.board.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcTemplatePostRepositoryImpl implements PostRepository{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplatePostRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Post> rowMapper = (rs, rowNum) -> {
        Post post = new Post();
        post.setId(rs.getLong("id"));
        post.setTitle(rs.getString("title"));
        post.setContent(rs.getString("content"));
        post.setAuthor(rs.getString("author"));
        post.setCreatedAt(rs.getTimestamp("created_at"));
        post.setUpdatedAt(rs.getTimestamp("updated_at"));
        return post;
    };


    @Override
    public void save(Post post) {
        String sql = "INSERT INTO posts(title, content, author, created_at) VALUES(?, ?, ?, CURRENT_TIMESTAMP)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContent());
            ps.setString(3, post.getAuthor());
            return ps;
        }, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        post.setId(id);
        System.out.println("save post : " + post.getId());
    }

    @Override
    public Optional<Post> findById(Long id) {
        return jdbcTemplate.query("select * from posts where id = ? ", rowMapper,id).stream().findFirst();
    }

    @Override
    public List<Post> findAll() {
        return jdbcTemplate.query("select * from posts", rowMapper);
    }

    @Override
    public void update(Post post) {
        String sql = "UPDATE posts SET title = ?, content = ?, updated_at = NOW() WHERE id = ?";
        jdbcTemplate.update(sql, post.getTitle(), post.getContent(), post.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM posts WHERE id = ?", id);
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(1) FROM posts";
        List<Integer> result = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt(1));
        return result.get(0);
    }
}
