package examples.board.repository;

import examples.board.domain.Post;

import java.util.HashMap;

public class MemoryPostRepositoryImpl implements PostRepository{
    private static final HashMap<String, Post> postStore = new HashMap<>();

    @Override
    public void save(Post post) {
        postStore.put(post.getTitle(), post);
    }

    @Override
    public Post findByTitle(String title) {
        if(postStore.containsKey(title))
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
