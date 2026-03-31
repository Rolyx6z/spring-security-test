package train.springsecuritytest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MakeHashController {

    // SecurityConfigでBean定義しているPasswordEncoderを持ってくる
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping(value="/makehash", params="str")
    public String index(@RequestParam("str")String str){
        String encodedPassword = passwordEncoder.encode(str);
        System.out.println(encodedPassword);

        return encodedPassword;
    }

}
