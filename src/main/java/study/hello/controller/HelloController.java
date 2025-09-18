package study.hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class HelloController {
    // Static
    @GetMapping("hello")
    public String hello(Model model){
        model.addAttribute("data","spring!!!");
        return "hello";
    }
    
    // MVC
    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam(value = "name",required = false) String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }

    // API
    @GetMapping("hello-string")
    @ResponseBody // http 통신 프로토콜 body 부에 데이터를 직접 넣어주겠단 의미
    public String helloString(@RequestParam(value = "name", required = true) String name){
        return "hello" + name;
    }

    @GetMapping("hello-api")
    @ResponseBody // http 통신 프로토콜 body 부에 데이터를 직접 넣어주겠단 의미
    public Hello helloApi(@RequestParam("name") String name){
        Hello hello = new Hello();
        for (int i = 0; i < 10; i++) {
            hello.setName(name + String.valueOf(i));
        }
        return hello;
    }

    static class Hello{
        private ArrayList<String> nameList = new ArrayList<>();

        public String getName() {
            return nameList.toString();
        }

        public void setName(String name) {
            this.nameList.add(name);
        }
    }
}
