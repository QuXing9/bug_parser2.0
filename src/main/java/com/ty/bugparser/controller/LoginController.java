package com.ty.bugparser.controller;

import com.ty.bugparser.pojo.User;
import com.ty.bugparser.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(String username,
                        String password,
                        Model model,
                        HttpSession session) {

        // 在这里做密码验证
        User user = userService.getUserByUsernameAndPassword(username, password);
        System.out.println(user);
        if (user != null) {
            // 通过验证，保存Session，并跳转至 dashbord 页面
            session.setAttribute("loginUser", user.getUsername());
            return "redirect:/main.html";

        } else {
            // 没有通过，提示密码错误，并跳转至登录页面
            model.addAttribute("errorMsg", "用户名或密码错误！");
            return "index";
        }
    }

    @RequestMapping("/logout")
    public String userLogout(HttpSession session, Model model) {
        session.invalidate();
        model.addAttribute("errorMsg", "您已注销，请重新登录！");
        return "/index.html";
    }
}
