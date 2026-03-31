package train.springsecuritytest.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController //コントローラーであることを示す、ControllerはHTML、RestControllerはXML,JSONを返す
@RequestMapping("/api") //クラス全体の共通パスを指定する
public class DemoController {

    @GetMapping("/hello") // /api/helloに対応
    public String hello(){
        return "Hello Everyone";
    }

    // メソッドを呼び出す前に、認可条件を評価する
    // 満たしてない場合はAccessDeniedExceptionを発生させメソッドを実行しない
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String userOnly(){
        return "Hello User";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminOnly(){
        return "Hello Admin";
    }
}
