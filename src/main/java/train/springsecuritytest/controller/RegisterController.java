package train.springsecuritytest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import train.springsecuritytest.form.AccountForm;
import train.springsecuritytest.service.RegisterService;

@Controller
public class RegisterController {
    @Autowired
    RegisterService registerService;

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("accountForm",new AccountForm());
        return "register";
    }

    @PostMapping("/register")
    public String newAccountRegister(@ModelAttribute @Validated AccountForm accountForm, BindingResult result, Model model){
        if(result.hasErrors()){
            return "register";
        }

        try{
            registerService.register(accountForm.getUsername(),accountForm.getPassword());
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/login";
    }
}
