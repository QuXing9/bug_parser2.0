package com.ty.bugparser.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class UserLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取验证对象
        HttpSession session = request.getSession();
        Object username = session.getAttribute("loginUser");

        // 若验证对象为空，说明用户未登录，则设定提示信息，请求转发至首页，加拦截
        if (username == null) {
            request.setAttribute("errorMsg", "没有访问权限，请先登录！");
            request.getRequestDispatcher("/index.html").forward(request, response);
            return false;
        } else {
            return true;
        }
    }
}
