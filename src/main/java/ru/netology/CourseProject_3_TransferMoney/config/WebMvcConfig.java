package ru.netology.CourseProject_3_TransferMoney.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.netology.CourseProject_3_TransferMoney.resolver.ConfirmOperationParamHandlerMethodArgumentResolver;
import ru.netology.CourseProject_3_TransferMoney.resolver.DataFormParamHandlerMethodArgumentResolver;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new DataFormParamHandlerMethodArgumentResolver());
        resolvers.add(new ConfirmOperationParamHandlerMethodArgumentResolver());
    }
}
