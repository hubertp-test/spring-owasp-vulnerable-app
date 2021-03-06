package sec.project.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// Settings for h2-console
		http.csrf().disable();
		http.headers().frameOptions().sameOrigin();

		// FLAW 4: A5:2017-Broken Access Control
		http.authorizeRequests()
				.antMatchers("/")
				.permitAll()
				.antMatchers("/createnewaccount")
				.permitAll()
				.antMatchers("/h2-console", "/h2-console/**")
				.permitAll()
				//.antMatchers(HttpMethod.GET, "/lounge").hasAnyAuthority("TEACHER")
				.anyRequest()
				.authenticated();

		http.formLogin().permitAll().and().logout().permitAll().logoutUrl("/logout").logoutSuccessUrl("/");

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
