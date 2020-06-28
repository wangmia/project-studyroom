package club.banyuan.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author WM
 * @date 2020/6/23 11:00 上午
 * 描述信息：
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index(){
        return "studyroom/login";
    }

    @GetMapping("/studyroom/login")
    public String studyRoomLogin() {
        return "studyroom/login";
    }

    @GetMapping("/studyroom/register")
    public String studyRoomRegister() {
        return "studyroom/register";
    }

    @GetMapping("/studyroom/index")
    public String studyRoomIndex() {
        return "studyroom/index";
    }

    @GetMapping("/studyroom/book")
    public String studyRoomBook() {
        return "studyroom/book";
    }

    @GetMapping("/studyroom/history")
    public String studyRoomHistory() {
        return "studyroom/history";
    }

    @GetMapping("/studyroom/admin")
    public String studyRoomAdmin() {
        return "studyroom/admin";
    }

}
