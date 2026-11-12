package com.pe.advanced.api.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.StubMethod;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.isAbstract;

/** Temporary documentation server: MVC contracts only, with no service implementations or infrastructure. */
@SpringBootConfiguration(proxyBeanMethods = false)
@EnableAutoConfiguration
public class DocumentationApplication {

    static {
        // Money is written as a JSON string by the mapper in rest-api, so that no client parses a
        // price into a double and loses the scale. Saying so once here is what keeps the published
        // schema honest: the alternative is @Schema(type = "string") on every BigDecimal field,
        // which is an agreement maintained by whoever remembers, and three fields already did not.
        SpringDocUtils.getConfig().replaceWithClass(BigDecimal.class, String.class);
    }

    static void main(String[] args) {
        SpringApplication.run(DocumentationApplication.class, args);
    }

    @Bean
    OpenAPI apiInfo(@Value("${api.version}") String version) {
        // Avoid baking the temporary build server's random port into the published specification.
        return new OpenAPI().info(new Info().title("Advanced Spring API").version(version))
                .servers(List.of(new Server().url("/")));
    }

    /**
     * One group, because every operation in this contract is for the same client.
     * <p>
     * Splitting the document into a published group and a wider internal one is worth doing the
     * day there is an administrative or a service-to-service surface that must not reach the
     * published file. There is not one yet, and inventing an endpoint to demonstrate the mechanism
     * would be building a feature to justify a design.
     */
    @Bean
    GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder().group("public").pathsToMatch("/**").build();
    }

    @Bean
    static BeanDefinitionRegistryPostProcessor contractControllers() {
        return registry -> {
            final var scanner = new ClassPathScanningCandidateComponentProvider(false) {
                @Override
                protected boolean isCandidateComponent(AnnotatedBeanDefinition definition) {
                    return definition.getMetadata().isIndependent() && definition.getMetadata().isInterface();
                }
            };
            scanner.addIncludeFilter(new AnnotationTypeFilter(RequestMapping.class));
            final var contracts = scanner.findCandidateComponents("com.pe.advanced.api.rest").stream()
                    .sorted(Comparator.comparing(BeanDefinition::getBeanClassName)).toList();
            if (contracts.isEmpty()) {
                throw new IllegalStateException("No API contracts found");
            }
            for (BeanDefinition definition : contracts) {
                try {
                    final var contract = Class.forName(definition.getBeanClassName());
                    for (var method : contract.getMethods()) {
                        if (AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class) == null) {
                            throw new IllegalStateException("Missing HTTP mapping on " + method);
                        }
                    }
                    final var controller = new ByteBuddy().subclass(Object.class).implement(contract)
                            .name("com.pe.advanced.api.openapi.generated." + contract.getSimpleName() + "Controller")
                            .annotateType(AnnotationDescription.Builder.ofType(RestController.class).build())
                            .method(isAbstract()).intercept(StubMethod.INSTANCE)
                            .make().load(DocumentationApplication.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded();
                    registry.registerBeanDefinition(contract.getSimpleName(), new RootBeanDefinition(controller));
                } catch (Exception e) {
                    throw new IllegalStateException("Cannot document " + definition.getBeanClassName(), e);
                }
            }
        };
    }
}
