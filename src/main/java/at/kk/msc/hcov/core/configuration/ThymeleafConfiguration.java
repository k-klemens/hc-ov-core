package at.kk.msc.hcov.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;

@Configuration
public class ThymeleafConfiguration {

  @Bean
  public ITemplateEngine templateEngine() {
    return new TemplateEngine();
  }
}
