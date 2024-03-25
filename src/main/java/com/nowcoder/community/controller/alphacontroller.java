package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class alphacontroller {
    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayhello() {
        return "hello!";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData() {
        return alphaService.find();
    }

    //获取请求、响应对象

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        //获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ":" + value);


        }
        System.out.println(request.getParameter("code"));

        //返回响应数据
        response.setContentType("text/html;charset=utf-8");
        try (PrintWriter writer = response.getWriter();) {
            writer.write("<h1>牛客网<h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //  /student?current=1&limit=20
    @RequestMapping(path = "/student", method = RequestMethod.GET)
    @ResponseBody   //返回html不用加这个注解，不加这个注解，默认返回html
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    //  /student/123
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id) {
        System.out.println(id);
        return "one student";
    }


    // post 请求处理
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    //向浏览器响应动态html
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("name", "zjq");  //设置模板的路径和名字
        mav.addObject("age", 18);
        mav.setViewName("/demo/view");    //默认是html文件，所以实际为view.html
        return mav;

    }


    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getSchool(Model model) {
        model.addAttribute("name", "tongji");
        model.addAttribute("age", "100");
        return "/demo/view";  //返回值为view，返回的即为view的路径
    }


    //向浏览器响应json数据（异步请求）
    //Java对象->json字符串->JS对象


    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getemp() {
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "zjq");
        emp.put("age", "18");
        emp.put("salary", "10000000");
        return emp;        //自动把map转成字符串
    }

    @RequestMapping(path = "/emps", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getemps() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "zjq");
        emp.put("age", "18");
        emp.put("salary", "10000000");
        list.add(emp);
        emp = new HashMap<>();
        emp.put("name", "syn");
        emp.put("age", "19");
        emp.put("salary", "23000000");
        list.add(emp);

        return list;        //自动把list转成字符串
    }

    //cookies
    @RequestMapping(path = "/cookies/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookies(HttpServletResponse response) {
        //创建cookies
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());

        //设置cookies生效范围
        cookie.setPath("/community/alpha");
        //设置cookies生存时间
        cookie.setMaxAge(600);
        //发送cookies
        response.addCookie(cookie);
        return "set cookie";

    }

    @RequestMapping(path = "/cookies/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code) {
        System.out.println(code);
        return "get cookies";
    }


    //session示例
    @RequestMapping(path = "/session/set", method = RequestMethod.GET)
    @ResponseBody
    public String setSessison(HttpSession session) {
        session.setAttribute("id", 1);
        session.setAttribute("name", "Test");

        return "set session";


    }

    @RequestMapping(path = "/session/get", method = RequestMethod.GET)
    @ResponseBody
    public String getSessison(HttpSession session) {
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "session get";
    }

    // ajax示例
    @RequestMapping(path = "/ajax", method = RequestMethod.POST)
    @ResponseBody         //异步请求，不返回网页，返回字符串
    public String testAjax(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJSONString(0, "操作成功!");
    }
}
