package com.hyun.jobty.conf.swagger;

import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.conf.swagger.annotation.ApiErrorCode;
import com.hyun.jobty.global.accountValidator.annotation.AccountValidator;
import com.hyun.jobty.global.response.CommonReason;
import com.hyun.jobty.global.response.CommonResult;
import com.hyun.jobty.global.response.ResponseService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@OpenAPIDefinition(
        info = @Info(title = "Jobty API 명세서",
                description = """
                <Notice>
                <p>Jobty 서비스 API 명세서</p>
                <p>* Responses의 *200은 200으로 응답되지만 예상 가능한 에러 처리입니다.</p>""",
                version = "v1"))
@Configuration
public class SwaggerConfig {
    private ResponseService responseService;
    private final String domain_package = "com.hyun.jobty.domain.";
    private final String global_package = "com.hyun.jobty.global.";
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
    public GroupedOpenApi getFileApi(@Qualifier("swagger") OperationCustomizer operationCustomizer){
        return GroupedOpenApi.builder()
                .group("file")
                .packagesToScan(global_package + "file")
                .addOperationCustomizer(operationCustomizer)
                .build();
    }

    @Bean
    @Qualifier("swagger")
    public OperationCustomizer customize(){
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiErrorCode apiErrorCode = handlerMethod.getMethodAnnotation(ApiErrorCode.class);
            AccountValidator accountValidator = handlerMethod.getMethodAnnotation(AccountValidator.class);
            GetMapping getMapping = handlerMethod.getMethodAnnotation(GetMapping.class);
            ApiResponses responses = operation.getResponses();
            // 기본 에러 응답 example 적용
            // get 이외의 경우 400 적용
            if (getMapping == null) {
                setResponse("400", responses, ErrorCode.RequiredFields.getCommonReason());
            }else{
                // get일 경우 400 제거
                responses.remove("400");
            }

            // accountValidator 어노테이션이 붙은 메소드는 401 메시지 출력
            if (accountValidator != null){
                setResponse("401", responses, ErrorCode.IncorrectTokenId.getCommonReason());
            }

            // apiErrorCode 어노테이션 단 메소드 적용, 해당 메소드는 200으로 응답됨
            if (apiErrorCode != null){
                List<CommonReason> commonReasons = new ArrayList<>();
                for (ErrorCode errorCode : apiErrorCode.value()){
                    commonReasons.add(errorCode.getCommonReason());
                }
                ExampleDto exampleDto = ExampleDto.builder()
                        .commonReasons(commonReasons)
                        .build();
                // Response 적용
                setCustomResponse("*200", responses, exampleDto);
            }

            // 500 적용
            setResponse("500", responses, ErrorCode.FAIL.getCommonReason());
            // 404 제거
            responses.remove("404");
            return operation;
        };
    }

    private void setResponse(String status, ApiResponses responses){
        Content content = new Content();
//        MediaType mediaType = setErrorExample(List.of(commonReason));
        ApiResponse apiResponse = new ApiResponse();
        content.addMediaType("application/json", new MediaType());
        apiResponse.setContent(content);
        Schema<Object> schema = new Schema<>();
        schema.set$schema("#/components/schemas/SingleResultFindRes");
        new MediaType().setSchema(schema);
        apiResponse.set$ref("#/components/schemas/SingleResultFindRes");
        responses.addApiResponse(status, apiResponse);
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