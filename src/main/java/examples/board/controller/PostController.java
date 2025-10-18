package examples.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostController {
    @GetMapping("/board")
    public String board(){
        return "board";
    }

    @GetMapping("/post-form")
    public String postForm(){
        return "board/postForm";
    }

}
