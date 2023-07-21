package sn.kiwi.apiwebsms;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sn.kiwi.apiwebsms.config.RsakeysConfig;

@SpringBootApplication
//@EnableConfigurationProperties(RsakeysConfig.class)
@OpenAPIDefinition(
		info = @Info(title = "WEBSMS API", version = "1.0.0")
)
public class ApiWebsmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiWebsmsApplication.class, args);
	}

	/*@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}*/
}
