package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {

    // Logger log = LoggerFactory.getLogger(getClass()); // @Slfj 어노테이션과 같은 코드이다.

    @RequestMapping("/")
    public String home() {
        // home.html 을 찾아간다
        log.info("home controller");
        return "home";
    }
}
