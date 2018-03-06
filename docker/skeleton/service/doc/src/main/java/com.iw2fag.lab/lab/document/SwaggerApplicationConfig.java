package com.iw2fag.lab.lab.document;

import com.google.common.base.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@EnableSwagger2
public class SwaggerApplicationConfig {
    private static final Logger logger = LoggerFactory.getLogger(SwaggerApplicationConfig.class);
    ResponseMessage m1000 = new ApiResponseMessage(400, "General Error Code.", null);
    ResponseMessage mHttpInternalServerError = new ApiResponseMessage(500, "Internal Server Error", null);
    ResponseMessage mHttpSuccess = new ApiResponseMessage(200, "OK", null);


    private final String path = "/";
    private final String connectorPath = "//{connectorId}";
    private final String wsDocPath = "/WSDoc";
    private final String restUploadApplicationPath = "/apps";
    private final String restUserManagementPath = "/v2/users";
    //    private final String restTakeSnapshotPath = "session";
//    private final String restCapableDevicesPath = "/job";
    private final String restClientPath = "/client";


    // this is all the rest api

    @Bean
    public Docket restApi() {
        Set<String> restProtocols = new HashSet<String>();
        restProtocols.add("http");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("HPMC-REST")
                .pathMapping("/rest/")
                .apiInfo(apiInfo())
                .protocols(restProtocols)
                .enableUrlTemplating(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.iw2fag.lab.controller.impl"))
                .paths(excludeWsPath()).build()
                ;

    }

    // this is all the web socket api
    @Bean
    public Docket wsApi() {
        Set<String> wsProtocols = new HashSet<String>();
        wsProtocols.add("http");
        List<ResponseMessage> responseMessages = new ArrayList<ResponseMessage>();

        responseMessages.add(m1000);


        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("HPMC-WS")
                .pathMapping("/ws/")
                .apiInfo(apiInfo())
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .protocols(wsProtocols)
                .enableUrlTemplating(true)
                .select()
                .paths(includeWsPath()).build()
                ;

    }

    @Bean
    public Docket publicRestApiForUserManagement(){
        Set<String> restProtocols = new HashSet<String>();

        List<ResponseMessage> responseMessageList = new ArrayList<ResponseMessage>();

        String error1For400 = "Invalid arguments.";
        String error2For400 = "Invalid operation.";
        String error3For400 = "Invalid user or password.";

        String error1For404 = "";

        ResponseMessage mError400ForUser = new ApiResponseMessage(400, error1For400 + " | " +
                error2For400 + " | " + error3For400, null);

        responseMessageList.add(mError400ForUser);
        responseMessageList.add(mHttpSuccess);

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("HPMC-PUBLIC-USER-MANAGEMENT")
                .pathMapping("/rest/")
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .protocols(restProtocols)
                .enableUrlTemplating(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.iw2fag.lab.controller.v2.impl"))
                .paths(includePublicRestPathForUserManagement()).build();
    }

    @Bean
    public Docket publicRestApiForClient(){
        Set<String> restProtocols = new HashSet<String>();

        List<ResponseMessage> responseMessageList = new ArrayList<ResponseMessage>();
        responseMessageList.add(mHttpSuccess);

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("HPMC-PUBLIC-CLIENT")
                .pathMapping("/rest/")
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .protocols(restProtocols)
                .enableUrlTemplating(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.iw2fag.lab.controller.impl"))
                .paths(includePublicRestPathForClient()).build();
    }

    @Bean
    public Docket publicRestApiForUpload(){
        Set<String> restProtocols = new HashSet<String>();

        List<ResponseMessage> responseMessageList = new ArrayList<ResponseMessage>();
        String error1 =  "The application {0} could not be uploaded.";
        String error2 = "The application {0} couldn't be uploaded because the application file name contains one or more of the following invalid characters: \"&<;/\"";
        ResponseMessage mError400ForApp = new ApiResponseMessage(400, error1 + " | " + error2, null);


        //responseMessageList.add(m2110);
        responseMessageList.add(mError400ForApp);
        responseMessageList.add(mHttpSuccess);

        List<ResponseMessage> responseMessageListForGet = new ArrayList<ResponseMessage>();
        responseMessageListForGet.add(m1000);
        responseMessageListForGet.add(mHttpSuccess);

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("HPMC-PUBLIC-UPLOAD")
                .pathMapping("/rest/")
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
//                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
//                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .globalResponseMessage(RequestMethod.GET, responseMessageListForGet)
                .protocols(restProtocols)
                .enableUrlTemplating(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.iw2fag.lab.controller.impl"))
                .paths(includePublicRestPathForUpload()).build();
    }

    private Predicate<String> includePublicRestPathForUserManagement(){
        return new Predicate<String>() {
            @Override
            public boolean apply(String input){

                return input.startsWith(restUserManagementPath);

            }

        };
    }

    private Predicate<String> includePublicRestPathForClient(){
        return new Predicate<String>() {
            @Override
            public boolean apply(String input){
                if(input.startsWith(restClientPath)){
                    return input.startsWith("/client/login") || input.startsWith("/client/logout");
                }
                return false;
            }

        };
    }

    private Predicate<String> includePublicRestPathForUpload(){
        return new Predicate<String>() {
            @Override
            public boolean apply(String input){
                return input.equals(restUploadApplicationPath);
            }

        };
    }

    private Predicate<String> includeWsPath() {
        return new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.equals(path)||input.equals(connectorPath) || input.equals(wsDocPath);
            }
        };
    }

    private Predicate<String> excludeWsPath() {
        return new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return !input.equals(path)&& !input.equals(connectorPath) && !input.equals(wsDocPath);
            }
        };
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo("HP Mobile Center LabName API Reference",
                "API Documentation for HP Mobile Center LabName", "2.00","",
                "", "",
                "#");
        return apiInfo;
    }

}

