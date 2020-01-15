package com.webank.weid.demo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ListVendorExtension;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置类.
 * @author darwindu
 * @date 2019/12/26
 **/
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "mconfig", name = "swagger-ui-open", havingValue = "true")
public class SwaggerConfig {

    /**
     * 构建swagger对象.
     * @return
     */
    @Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .enable(true)
            .apiInfo(apiInfo())
            .groupName("default")
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.webank.weid.demo.controller"))
            .paths(PathSelectors.any())
            .build();
    }

    /*@Bean(value = "otherRestApi")
    public Docket otherRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .enable(true)
            //.apiInfo(apiInfo())
            .groupName("其他相关接口")
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.webank.weid.demo.controller.other"))
            .paths(PathSelectors.any())
            .build();
    }*/

    private ApiInfo apiInfo() {

        List<VendorExtension> extensions = new ArrayList<VendorExtension>();
        VendorExtension vendorExtension = new ListVendorExtension("base-url", null);
        extensions.add(vendorExtension);


        return new ApiInfoBuilder()
            .title("WeId Sample API")
            .description("您可以在此快速了解WeIdentity的参考场景、体验Demo、快速体验WeIdentity的核心功能。 "
                + "如果您是开发人员，还可以进一步了解WeIdentity的参考实现，以及深入了解SDK的使用方式")
            //.contact(new Contact("weid docs","https://weidentity.readthedocs.io/zh_CN/latest/README.html","weidentity@webank.com"))
            .termsOfServiceUrl(null)
            .license(null)
            //.licenseUrl("https://github.com/WeBankFinTech/WeIdentity/blob/master/COPYING")
            //.license("Find out more about weid")
            //.licenseUrl("https://github.com/WeBankFinTech/WeIdentity")
            .version(null)
            .extensions(extensions)
            .build();
    }

    /**
     * swagger ui界面配置.
     * @return
     */
    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
            .deepLinking(true)
            .displayOperationId(false)
            //设置Models展示
            .defaultModelsExpandDepth(-1)
            .defaultModelExpandDepth(1)
            .defaultModelRendering(ModelRendering.EXAMPLE)
            .displayRequestDuration(false)
            //自动展开接口列表
            .docExpansion(DocExpansion.LIST)
            .filter(false)
            .maxDisplayedTags(null)
            //OperationsSorter.ALPHA – 按路径按字母顺序对API端点进行排序
            //OperationsSorter.METHOD – 按方法按字母顺序对API端点进行排序
            .operationsSorter(OperationsSorter.METHOD)
            .showExtensions(false)
            .tagsSorter(TagsSorter.ALPHA)
            .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
            .validatorUrl(null)
            .build();
    }
}
