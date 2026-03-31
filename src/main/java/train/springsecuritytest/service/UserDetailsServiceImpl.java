package train.springsecuritytest.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import train.springsecuritytest.model.Account;
import train.springsecuritytest.repository.AccountRepository;

@Component
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException{
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("ユーザー" + username + "が見つかりません");
                    throw new UsernameNotFoundException(username);
                }
        );

        //認証情報の作成
        return User.withUsername(
                account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole())
                .disabled(!account.isEnabled())
                .build();
    }
}
