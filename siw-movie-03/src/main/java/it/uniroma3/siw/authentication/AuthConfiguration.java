package it.uniroma3.siw.authentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

import static it.uniroma3.siw.model.Credentials.ADMIN_ROLE;
//import static it.uniroma3.siw.model.Credentials.DEFAULT_ROLE;

@Configuration
@EnableWebSecurity
public class AuthConfiguration extends WebSecurityConfigurerAdapter {

	/**
	 * La sorgente dati (che contiene le credenziali) è 
	 * iniettata automaticamente
	 */
	@Autowired
	DataSource datasource;

	/**
	 * Questo metodo contiene le impostazioni della configurazione
	 * di autenticatzione e autorizzazione.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		// AUTORIZZAZIONE: qui definiamo chi può accedere a cosa
		.authorizeRequests()
		// chiunque (autenticato o no) può accedere alle pagine index, login, register, ai css e alle immagini
		.antMatchers(HttpMethod.GET, "/", "/index", "/login", "/register", "/css/**", "/images/**", "favicon.ico").permitAll()
		// chiunque (autenticato o no) può mandare richieste POST al punto di accesso per login e register 
		.antMatchers(HttpMethod.POST, "/login", "/register").permitAll()
		// solo gli utenti autenticati con ruolo ADMIN possono accedere a risorse con path /admin/**
		.antMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(ADMIN_ROLE)
		.antMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(ADMIN_ROLE)
		// tutti gli utenti autenticati possono accere alle pagine rimanenti 
		.anyRequest().authenticated()
		.and().exceptionHandling().accessDeniedPage("/index")

		// LOGIN: qui definiamo come è gestita l'autenticazione
		// usiamo il protocollo formlogin 
		.and().formLogin()
		// la pagina di login si trova a /login
		.loginPage("/login")
		// se il login ha successo, si viene rediretti al path /default
		.defaultSuccessUrl("/success", true)

		// LOGOUT: qui definiamo il logout
		.and()
		.logout()
		// il logout è attivato con una richiesta GET a "/logout"
		.logoutUrl("/logout")
		// in caso di successo, si viene reindirizzati alla home
		.logoutSuccessUrl("/")
		.invalidateHttpSession(true)
		.deleteCookies("JSESSIONID")
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.clearAuthentication(true).permitAll();
	}

	/**
	 * Questo metodo definisce le query SQL per ottenere username e password
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
		.dataSource(this.datasource)
		//query per recuperare username e ruolo
		.authoritiesByUsernameQuery("SELECT username, role FROM credentials WHERE username=?")
		//query per username e password. Il flag boolean flag specifica se l'utente user è abilitato o no (va sempre a true)
		.usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
	}

	/**
	 * Questo metodo definisce il componente "passwordEncoder", 
	 * usato per criptare e decriptare la password nella sorgente dati.
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
