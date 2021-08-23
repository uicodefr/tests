package test.uicode.springgateway.config;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GatewayConfig.class);

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(p -> p.path("/test1/**")
                .filters(f -> f.addRequestHeader("request", "request")
                    .addResponseHeader("response", "response")
                    .rewritePath("/test1/?(?<segment>.*)", "/toto/${segment}")
                    .filters(monFiltrePerso()))
                .uri("https://google.com"))
            .route(p -> p.path("/hello/**").uri("https://google.com"))
            .route(p -> p.path("/test2/**")
                .filters(f -> f.rewritePath("/test2/?(?<segment>.*)", "/buildsv6/images/wide1920/4/8/4/${segment}"))
                .uri("https://cdn.futura-sciences.com"))// 484450e69f_50172577_betes-science-ecureuil-gris.jpg
            .route("ecureil",
                    p -> p.path("/test3/**")
                        .filters(f -> f.rewritePath("/test3/?(?<segment>.*)",
                                "/buildsv6/images/wide1920/4/8/4/484450e69f_50172577_betes-science-ecureuil-gris.jpg")
                            .filter(loggingFilter()))
                        .uri("https://cdn.futura-sciences.com"))
            .route("manytools",
                    p -> p.path("/test4/**")
                        .filters(f -> f.rewritePath("/test4/?(?<segment>.*)", "/http-html-text/http-request-headers/")
                            .filter(loggingFilter()))
                        .uri("https://manytools.org"))
            .route("whatismybrowser.com", p -> p.path("/test5/**")
                .filters(f -> f.rewritePath("/test5/?(?<segment>.*)", "/detect/what-http-headers-is-my-browser-sending")
                    .filter(loggingFilter()))
                .uri("https://www.whatismybrowser.com"))
            .build();
    }

    private GatewayFilter monFiltrePerso() {
        return (exchange, chain) -> {
            LOGGER.info("monFiltrePerso ");
            return exchange.getPrincipal().flatMap(principal -> {
                exchange.getResponse().getHeaders().add("perso-resp", "YEP");
                exchange.getResponse().getHeaders().add("username", principal.getName());
                exchange.getRequest().mutate().headers(h -> {
                    h.add("perso-req", "YOP");
                    h.add("username", principal.getName());
                });
                return chain.filter(exchange);
            });
        };
    }

    private GatewayFilter loggingFilter() {
        return (exchange, chain) -> {
            Set<URI> uris = exchange.getAttributeOrDefault(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR,
                    Collections.emptySet());
            String originalUri = (uris.isEmpty()) ? "Unknown" : uris.iterator().next().toString();
            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            URI routeUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
            Map<String, Object> toto = exchange.getAttributes();
            LOGGER.info("Incoming request " + originalUri + " is routed to id: " + route.getId() + ", uri:" + routeUri);
            return chain.filter(exchange);
        };
    }

}
