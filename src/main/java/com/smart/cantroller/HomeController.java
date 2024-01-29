package com.smart.cantroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.dao.UserReposetory;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserReposetory userReposetory;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About - Smart Contact Manager");
        return "about";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Register - Smart Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    // this handler for register user
    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
            HttpSession session) {

        try {

            if (!agreement) {
                System.out.println("You not  agreed the terms and condition");
                throw new Exception("You not  agreed the terms and condition");
            }

            if (result1.hasErrors()) {
                System.out.println("ERROR " + result1.toString());
                model.addAttribute("user", user);
                return "signup";
            }

            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageUrl("default.png");
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            System.out.println("Agreement" + agreement);
            System.out.println("User" + user);

            User result = this.userReposetory.save(user);

            model.addAttribute("user", new User());

            session.setAttribute("message", new Message("Successfully Registred !!", "alert-success"));
            return "signup";

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Somthing went Wrong !!" + e.getMessage(), "alert-danger"));
            return "signup";
        }

    }

}
