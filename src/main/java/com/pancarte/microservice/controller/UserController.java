package com.pancarte.microservice.controller;


import com.pancarte.microservice.model.User;
import com.pancarte.microservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


@Controller
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService ){

        this.userService = userService;

    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView model = new ModelAndView();
        model.addObject("view", "login");
        model.addObject("userName", "0");
        //model.setViewName("user/login");
        model.setViewName("index");
        return model;
    }

    @RequestMapping(value = {"/signup"}, method = RequestMethod.GET)
    public ModelAndView signup() {
        ModelAndView model = new ModelAndView();
        User user = new User();
        model.addObject("user", user);
        // model.setViewName("user/signup");
        model.addObject("view", "signup");
        model.setViewName("index");

        model.addObject("userName", "0");

        return model;
    }

    @RequestMapping(value = {"/signup"}, method = RequestMethod.POST)
    public ModelAndView createUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());

        if (userExists != null) {
            bindingResult.rejectValue("email", "error.user", "L'email existe déja");
        }
        if (bindingResult.hasErrors()) {
            // model.setViewName("user/signup");
            model.addObject("view", "signup");
            model.setViewName("index");
        } else {
            userService.saveUser(user);
            model.addObject("msg", "L'utilisateur à été enregistré");
            model.addObject("user", new User());

            model.addObject("userName", "0");

            //model.setViewName("user/signup");
            model.addObject("view", "login");
            model.setViewName("index");

        }

        return model;
    }

    @RequestMapping(value = {"/logged"}, method = RequestMethod.GET)
    public String logged() {


        return "redirect:/loggedhome";
    }


    @RequestMapping(value = {"/access_denied"}, method = RequestMethod.GET)
    public ModelAndView accessDenied() {
        ModelAndView model = new ModelAndView();
        model.setViewName("errors/access_denied");
        return model;
    }

    @RequestMapping(value = {"/error"}, method = RequestMethod.GET)
    public ModelAndView error() {
        ModelAndView model = new ModelAndView();
        model.setViewName("errors/error");
        return model;
    }
}
