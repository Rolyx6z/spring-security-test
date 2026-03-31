package train.springsecuritytest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import train.springsecuritytest.security.RestAccessDeniedHandler;
import train.springsecuritytest.security.RestAuthenticationEntryPoint;

// 参考ページ
// https://tech.nri-net.com/entry/alittle_serious_about_spring_security

@Configuration          // 設定ファイルであることを示す
@EnableWebSecurity      // Webページ単位でのセキュリティを有効化する
@EnableMethodSecurity   // メソッド単位でのセキュリティを有効化する
public class SecurityConfig{

    @Bean       // DIコンテナにSecurityFilterChainクラスを格納する
                // DIコンテナ:インスタンスをおいておける箱
    @Order(1)   // 処理の優先度を示す 1番目
    public SecurityFilterChain apiChain(
            final HttpSecurity http,
            final RestAuthenticationEntryPoint entryPoint,
            final RestAccessDeniedHandler accessDeniedHandler
    ) throws Exception{
        http
                .securityMatcher("/api/**")
                // CSRF保護を無効にする
                .csrf(csrf -> csrf.disable())
                // Sessionを保持しない
                // 保持したい場合はこの記述を消せばOK
                .sessionManagement(s ->
                        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 認可(Authorize)
                .authorizeHttpRequests(auth -> auth
                        // すべての人に許可
                        .requestMatchers("/api/hello").permitAll()
                        // 上記以外の全てのリクエストは認証が必要
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        // 401
                        .authenticationEntryPoint(entryPoint)
                        // 403
                        .accessDeniedHandler(accessDeniedHandler)
                )
                // デフォルトの設定を使用
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form.disable()
                );

        return http.build();
    }

    @Bean
    @Order(2) //2番目
    // 複数のFilterを組み合わせてクライアントからのリクエストに対してセキュリティチェックを行う
    public SecurityFilterChain securityFilterChain(final HttpSecurity http)
        throws Exception{
        http

                // 認可 (Authorization)
                // 対象のURLがどのユーザーに許可されるかを指定
                .authorizeHttpRequests(auth ->auth
                        .requestMatchers("/","/public/**").permitAll()  // すべての人
                        .requestMatchers("/user/**").authenticated()    // 認証(ログイン)をしている人のみ
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // ADMINをもつユーザーのみ
                        .anyRequest().authenticated()
                )
                // 認証 (Authentication)
                .formLogin(form -> form
                        .permitAll()            // すべての人
                )
                // ログアウト
                .logout(logout->logout
                        .logoutSuccessUrl("/")  // ログアウト後の遷移先
                        .permitAll()            // すべての人
                );

        // 認可、認証、ログアウトなどのバラバラのセキュリティを一つにまとめてくれる
        return http.build();
    }

    @Bean   // ユーザー情報の定義
            // UserDetailsServiceクラスをConfigで定義しておくことで自動でユーザー認証を行える
    public UserDetailsService userDetailsService() {

        // UserDetailsはユーザー情報を入れておく箱のこと
        final UserDetails alice = User.withUsername("alice")
                // noopはハッシュ化をしないという意味
                .password("{noop}password123")
                .roles("ADMIN")
                .build();

        final UserDetails bob = User.withUsername("bob")
                .password("{noop}password123")
                .roles("USER")
                .build();

        // メモリ上に認証するユーザー情報を登録しておく
        return new InMemoryUserDetailsManager(alice,bob);
    }
}
