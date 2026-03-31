package train.springsecuritytest.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class AccountForm {
    @NotBlank(message = "ユーザー名が未入力です")
    private String username;

    @NotBlank(message = "パスワードが未入力です")
    private String password;
}
