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

@Slf4j
@Controller
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;

//    @RequestMapping("ModifyPassword")
//    @ResponseBody
//    public String modifyPassword(HttpSession session,
//                                 String oldPassword,
//                                 String newPassword,
//                                 Model model) {
//        // 从session中获取用户名，并获取到User对象
//        String username = (String)session.getAttribute("loginUser");
//        User user = userService.getUserByUsernameAndPassword(username, oldPassword);
//        if (user == null) {
//            // 假如用户输入的旧密码错误，则返回并直接提示
//            model.addAttribute("errorMsg", "旧密码输入有误，请重试");
//            return "/User/ModifyPasswordPage";
//        }
//
//        // 将新密码赋给User对象，并提交
//        user.setPassword(newPassword);
//        int updateUserResult = userService.updateUser(user);
//        if (updateUserResult != 1) {
//            // 说明更新失败
//            model.addAttribute("errorMsg", "密码修改失败，请重试");
//            return "/User/ModifyPasswordPage";
//        } else {
//            // 更新成功
//            session.invalidate();
//            model.addAttribute("errorMsg", "密码已修改，请重新登录！");
//            return "/index.html";
//        }
//    }
}
