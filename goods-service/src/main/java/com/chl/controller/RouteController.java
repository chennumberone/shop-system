package com.chl.controller;

import com.chl.config.AccessLimit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chl
 * @date 2020/7/5 10:53
 */
@Controller
public class RouteController {
    @RequestMapping("toLogin")
    public String Login(){
        return "login";
    }

    @RequestMapping("toList")
    public String List(){
        return "list";
    }


    @AccessLimit(seconds = 15,maxCount = 4)
    @RequestMapping("access")
    @ResponseBody
    public String T1(){
        return "123";
    }
}
