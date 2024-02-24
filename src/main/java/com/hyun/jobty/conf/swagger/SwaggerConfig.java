package com.hyun.jobty.conf.swagger;

import com.hyun.jobty.global.annotation.AccountValidator;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.global.response.CommonReason;
import com.hyun.jobty.global.response.CommonResult;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.conf.swagger.annotation.ApiErrorCode;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@OpenAPIDefinition(
        info = @Info(title = "Jobty API 명세서",
                description = "Jobty 서비스 API 명세서",
                version = "v1"))
@Configuration
public class SwaggerConfig {
    private ResponseService responseService;
    private final String domain_package = "com.hyun.jobty.domain.";
    public SwaggerConfig(ResponseService responseService){
        this.responseService = responseService;
    }

    @Bean
    public OpenAPI openAPI(){
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement));
    }

    @Bean
    public GroupedOpenApi getMemberApi(@Qualifier("swagger") OperationCustomizer operationCustomizer){
        return GroupedOpenApi.builder()
                .group("member")
//                .pathsToMatch("/api/member/**")
                .packagesToScan(domain_package + "member")
                .addOperationCustomizer(operationCustomizer)
                .build();
    }

    @Bean
    public GroupedOpenApi getSettingApi(@Qualifier("swagger") OperationCustomizer operationCustomizer){
        return GroupedOpenApi.builder()
                .group("setting")
                .packagesToScan(domain_package + "setting.detail")
                .addOperationCustomizer(operationCustomizer)
                .build();
    }

    @Bean
    public GroupedOpenApi getMenuApi(@Qualifier("swagger") OperationCustomizer operationCustomizer){
        return GroupedOpenApi.builder()
                .group("menu")
                .packagesToScan(domain_package + "setting.menu")
                .addOperationCustomizer(operationCustomizer)
                .build();
    }

    @Bean
    public GroupedOpenApi getBlogApi(@Qualifier("swagger") OperationCustomizer operationCustomizer){
        return GroupedOpenApi.builder()
                .group("blog")
                .packagesToScan(domain_package + "blog")
                .addOperationCustomizer(operationCustomizer)
                .build();
    }

    @Bean
    @Qualifier("swagger")
    public OperationCustomizer customize(){
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiErrorCode apiErrorCode = handlerMethod.getMethodAnnotation(ApiErrorCode.class);
            AccountValidator accountValidator = handlerMethod.getMethodAnnotation(AccountValidator.class);
            ApiResponses responses = operation.getResponses();
            // 기본 에러 응답 example 적용
            // 400 적용
            if (accountValidator == null){
                // accountValidator 어노테이션 단 메소드 적용
                setResponse("400", responses, ErrorCode.FAIL.getCommonReason());
            } else {
                setResponse("400", responses, ErrorCode.IncorrectTokenId.getCommonReason());
            }

            // apiErrorCode 어노테이션 단 메소드 적용
            if (apiErrorCode != null){
                List<CommonReason> commonReasons = new ArrayList<>();
                for (ErrorCode errorCode : apiErrorCode.value()){
                    commonReasons.add(errorCode.getCommonReason());
                }
                ExampleDto exampleDto = ExampleDto.builder()
                        .commonReasons(commonReasons)
                        .build();
                // Response 적용
                setCustomResponse("400", responses, exampleDto);
            }

            // 500 적용
            setResponse("500", responses, ErrorCode.FAIL.getCommonReason());

//            operation.setResponses(new ApiResponses());
            return operation;
        };
    }

    private void setResponse(String status, ApiResponses responses, CommonReason commonReason){
        Content content = new Content();
        MediaType mediaType = setErrorExample(List.of(commonReason));
        ApiResponse apiResponse = new ApiResponse();
        content.addMediaType("application/json", mediaType);
        apiResponse.setContent(content);
        responses.addApiResponse(status, apiResponse);
    }

    private void setCustomResponse(String status, ApiResponses responses, ExampleDto exampleDto){
        Content content = new Content();
        MediaType mediaType = setErrorExample(exampleDto.getCommonReasons());
        ApiResponse apiResponse = new ApiResponse();
        content.addMediaType("application/json", mediaType);
        apiResponse.setContent(content);
        responses.addApiResponse(status, apiResponse);
    }

    private MediaType setErrorExample(List<CommonReason> commonReasons){
        MediaType mediaType = new MediaType();
        for (CommonReason commonReason : commonReasons){
            CommonResult commonResult = responseService.getFailResult(commonReason);
            Example example = new Example();
            example.description(commonReason.getRemark());
            // example 응답 객체 설정
            example.setValue(commonResult);
            mediaType.addExamples(commonReason.getName(), example);
        }
        return mediaType;
    }
}