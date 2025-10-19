package examples.board.service;

import examples.board.domain.Post;
import examples.board.dto.PostForm;
import examples.board.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(@Qualifier("jdbcTemplatePostRepositoryImpl") PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    public void create(Post post, PostForm form) {
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        post.setAuthor(form.getAuthor());
        postRepository.save(post);
    }

    @Override
    public void update(Post post,PostForm form) {
        postRepository.findById(post.getId()).orElseThrow(() -> new IllegalArgumentException("post not found"));
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        post.setAuthor(form.getAuthor());
        postRepository.update(post);
    }

    @Override
    public void delete(Long id) {
        postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("post not found"));
        postRepository.delete(id);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Post get(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public long count() {
        return postRepository.count();
    }


}
