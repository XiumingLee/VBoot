package cn.xiuminglee.vboot.modules.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Xiuming Lee
 * @Description
 */
@Controller
public class MainController {

    @RequestMapping("/")
    public void mainPage(HttpServletResponse response) throws IOException {
        response.sendRedirect("index.html");
    }
}
