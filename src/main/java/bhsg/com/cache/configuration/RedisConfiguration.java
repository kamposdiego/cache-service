package bhsg.com.cache.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackages = "bhsg.com.api.repository")
@Profile("!test")
public class RedisConfiguration {

}
