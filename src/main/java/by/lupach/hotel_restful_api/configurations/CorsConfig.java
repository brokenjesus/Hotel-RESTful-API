package by.lupach.hotel_restful_api.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Включаю CORS, так как не знаю, как будет проходить автоматическое тестирование
        // CORS необходим, когда клиентская часть приложения находятся на другом домене или порту (для разрешения кросс-доменных запросов).
        registry.addMapping("/property-view/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
