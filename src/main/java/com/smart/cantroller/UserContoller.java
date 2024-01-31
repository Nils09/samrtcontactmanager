package com.smart.cantroller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.dao.UserReposetory;
import com.smart.entities.User;



@Controller
@RequestMapping("/user")
public class UserContoller {

  @Autowired
  private UserReposetory userReposetory;

  @RequestMapping("/index")
  public String dashbord(Model model, Principal principal) {

    String userName = principal.getName();
    System.out.println("USERNAME " + userName);

    User user = userReposetory.getUserByUserName(userName);
    System.out.println("USER " + user);
    model.addAttribute("user", user);


    return "normal/user_dashbord";
  }

}
