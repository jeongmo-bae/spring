package examples.board.repository;

import examples.board.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class MemoryPostRepositoryImpl implements PostRepository{
    private static final HashMap<Long, Post> postStore = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public void save(Post post) {
        post.setId(++sequence);
        postStore.put(post.getId(), post);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(postStore.get(id));
    }

    @Override
    public List<Post> findAll() {
        return new ArrayList<>(postStore.values());
    }

    @Override
    public void update(Post post) {
        postStore.put(post.getId(), post);
    }

    @Override
    public void delete(Long id) {
        postStore.remove(id);
    }

    @Override
    public int count() {
        return postStore.size();
    }
}
