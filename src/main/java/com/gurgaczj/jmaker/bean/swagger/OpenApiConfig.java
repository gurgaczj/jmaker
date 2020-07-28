package com.gurgaczj.jmaker.bean.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.description}")
    private String appDesc;

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(createAppInfo())
                .path("/login", new PathItem()
                        .post(new Operation().description("Authenticates user with giver username and password. Only available through " +
                                MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .responses(new ApiResponses().addApiResponse(
                                "200 OK", new ApiResponse().description("return Authorization header with bearer jwt"))
                            .addApiResponse(
                                    "401 UNAUTHORIZED", new ApiResponse().description("when username or password is wrong"))))
                        .addParametersItem(new Parameter().name("username").description("user's username"))
                        .addParametersItem(new Parameter().name("password").description("user's password")));
    }

    private Info createAppInfo() {
        return new Info()
                .description(appDesc)
                .title(appName);
    }

    private class LoginForm{
        private String username;
        private String password;

        public LoginForm(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
