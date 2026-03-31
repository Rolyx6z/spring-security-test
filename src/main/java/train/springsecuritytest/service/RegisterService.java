package train.springsecuritytest.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import train.springsecuritytest.model.Account;
import train.springsecuritytest.repository.AccountRepository;

import java.util.Optional;

@Service
@Transactional
public class RegisterService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(String username,String password)
        throws RuntimeException{
        Optional<Account> existingAcount = accountRepository.findByUsername(username);

        if (existingAcount.isPresent()){
            throw new RuntimeException("このユーザー名はすでに使用されています:" + username);
        }

        Account newAccount = new Account();
        newAccount.setUsername(username);
        newAccount.setPassword(passwordEncoder.encode(password));
        newAccount.setRole("USER");
        newAccount.setEnabled(true);

        accountRepository.save(newAccount);
        System.out.println("ユーザー" + username + "の登録が完了しました!!");
    }
}
