package bhsg.com.cache.configuration;

import bhsg.com.cache.interceptor.RequestAndCorrelationIdInterceptor;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {

    private final RequestAndCorrelationIdInterceptor interceptor;

    public GrpcServerConfig(RequestAndCorrelationIdInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Bean
    public GrpcServerConfigurer serverConfigurer() {
        return serverBuilder -> serverBuilder.intercept(interceptor);
    }

}
