package examples.board.controller;

import examples.board.domain.Post;
import examples.board.dto.PostForm;
import examples.board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        List<Post> content = service.findAll();
        long total = service.count();
        model.addAttribute("posts", content);
        model.addAttribute("total", total);

        return "board/posts";
    }

    @GetMapping("/posts/new")
    public String postForm(
            Model model
    ){
        model.addAttribute("form", new PostForm());
        return "board/postFormNew";
    }

    @PostMapping("/posts")
    public String create(@ModelAttribute PostForm form) {
        service.create(new Post(), form);
        return "redirect:/posts";
    }

}
