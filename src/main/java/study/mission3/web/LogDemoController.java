package study.mission3.web;

import jakarta.inject.Provider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import study.mission3.common.MyLogger;

@Controller
@RequiredArgsConstructor
public class LogDemoController {
    private final LogDemoService logDemoService;
//    private final Provider<MyLogger> myLoggerProvider;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request){
        // MyLogger Proxy 객체가 주입 되어 있음 -> 실제 요청이 오면 그때, 진짜 빈을 요청하는 위임로직이 들어가 있음.
        // myLogger = class study.mission3.common.MyLogger$$SpringCGLIB$$0
        System.out.println("myLogger = " + myLogger.getClass());
//        MyLogger myLogger = myLoggerProvider.get();
        String requestURL = request.getRequestURL().toString();
        myLogger.setRequestURL(requestURL);
        myLogger.log("LogDemoController Test");

        logDemoService.logic("testId");

        return "OK";
    }
}
