package examples.board.controller;

import examples.board.domain.Post;
import examples.board.dto.PostForm;
import examples.board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {
    private final PostService service;

    @Autowired
    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping("/posts")
    public String board(
            @RequestParam(required = false) Long id,
            Model model
    ){
        if(id != null){
            List<Post> posts = service
                    .get(id)
                    .map(List::of)
                    .orElse(new ArrayList<>());
            long total = posts.size(); ;
            model.addAttribute("posts", posts);
            model.addAttribute("total", total);
        }else {
            List<Post> posts = service.findAll();
            long total = service.count();
            model.addAttribute("posts", posts);
            model.addAttribute("total", total);
        }
        return "board/posts";
    }

    @GetMapping("/posts/new")
    public String postFormNew(Model model){
        model.addAttribute("form", new PostForm());
        model.addAttribute("edit", false);
        return "board/postForm";
    }

    @PostMapping("/posts/new")
    public String create(@ModelAttribute PostForm form) {
        service.create(new Post(), form);
        return "redirect:/posts";
    }


    @GetMapping("/posts/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Post post = service.get(id).orElseThrow(() -> new IllegalArgumentException("post not found : " + id));
        model.addAttribute("post", post);
        return "board/postDetail";
    }


    @GetMapping("/posts/{id}/edit")
    public String postFormEdit(@PathVariable Long id, Model model){
        Post post = service.get(id).orElseThrow(() -> new IllegalArgumentException("post not found"));
        PostForm postForm = new PostForm();
        postForm.setTitle(post.getTitle());
        postForm.setContent(post.getContent());
        postForm.setAuthor(post.getAuthor());
        model.addAttribute("id", id);
        model.addAttribute("form", postForm);
        model.addAttribute("edit", true);
        return "board/postForm";
    }

    @PostMapping("/posts/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute PostForm form) {
        Post post = service.get(id).orElseThrow(() -> new IllegalArgumentException("post not found"));
        service.update(post, form);
        return "redirect:/posts";
    }

    @PostMapping("/posts/{id}/delete")
    public String delete(@PathVariable Long id) {
        Post post = service.get(id).orElseThrow(() -> new IllegalArgumentException("post not found"));
        service.delete(id);
        return "redirect:/posts";
    }



}
