package com.smart.cantroller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.domain.JpaSort.Path;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.UserReposetory;
import com.smart.entities.Contact;
import com.smart.entities.User;

@Controller
@RequestMapping("/user")
public class UserContoller {

  @Autowired
  private UserReposetory userReposetory;
  private java.nio.file.Path path;

  @ModelAttribute
  public void addCommonData(Model model, Principal principal) {
    String userName = principal.getName();
    System.out.println("USERNAME " + userName);

    User user = userReposetory.getUserByUserName(userName);
    System.out.println("USER " + user);
    model.addAttribute("user", user);
  }

  // dashboard home
  @RequestMapping("/index")
  public String dashboard(Model model, Principal principal) {
    model.addAttribute("title", "User-Dashboard");

    return "normal/user_dashbord";
  }

  // open add form handler
  @GetMapping("/add-contact")
  public String openAddContactForm(Model model) {
    model.addAttribute("title", "Add Contact");
    model.addAttribute("contact", new Contact());
    return "/normal/add_contact_form";
  }

  @PostMapping("process-contact")
  // processimg add contact form
  public String processContact(
      @ModelAttribute Contact contact,
      @RequestParam("profileImage") MultipartFile file,
      Principal principal) {
    try {
      String name = principal.getName();
      User user = this.userReposetory.getUserByUserName(name);

      // processing and uploding file

      if (file.isEmpty()) {
        // if the file is empty the try our message

        System.out.println("file is empty");
      } else {
        // file the file to folder and update the name to contact
        contact.setImage(file.getOriginalFilename());
        File saveFile = new ClassPathResource("static/img").getFile();

        path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("image is uploded");

      }

      contact.setUser(user);
      user.getContacts().add(contact);

      this.userReposetory.save(user);
      System.out.println("Data " + contact);

      System.out.println("Added to data base");
    } catch (Exception e) {
      System.out.println("ERROR " + e.getMessage());
      e.printStackTrace();

    }
    return "normal/add_contact_form";
  }

}
