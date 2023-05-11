package com.miachyn.watcherservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miachyn.watcherservice.dto.CurrencyDto;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableScheduling
public class CurrencyConfig {

    @Bean
    public List<CurrencyDto> currencies(ObjectMapper objectMapper) throws IOException {
        CurrencyDto[] currencies = objectMapper.readValue(
                new ClassPathResource("init/cryptocurrencies.json").getFile(),
                CurrencyDto[].class
        );
        return Arrays.asList(currencies);
    }

    @Bean
    public MessageSource validationMessageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/validation");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean localValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(validationMessageSource());
        return bean;
    }

}
