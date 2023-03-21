package boot.controller;

import boot.dto.UserRegistrationDto;
import boot.entity.User;
import boot.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {
    private UserService userService;

    public UserRegistrationController(UserService userService) {
        super();
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user")UserRegistrationDto registrationDto,BindingResult result,
			Model model) {
    	User existingUser = userService.findUserByEmail(registrationDto.getEmail());
		
		if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", 
            					null,"There is already an account registered with the same email");
        }
		
		if(result.hasErrors()) {
			model.addAttribute("user",registrationDto);
			return "redirect:/registration?fail";
		}
		userService.save(registrationDto);
		if(userService.findUserByEmail("admin@gmail.com") == null) {
			userService.admin();
		}
		return "redirect:/registration?success";
    }
}
