package bhsg.com.cache.logging;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Utility class for building traceable log context strings.
 */
@Slf4j
public final class LogContextUtils {

    private LogContextUtils(){
        //Default constructor.
    }

    public static String trace() {
        return String.format("requestId=%s, correlationId=%s",
                safe("X-Request-ID"),
                safe("X-Correlation-ID"));
    }

    private static String safe(String key) {
        return Optional.ofNullable(MDC.get(key)).orElse("N/A");
    }

    public static void infoWithTrace(String message, Object... args) {
        var argsWithTrace = Stream.concat(
                Stream.of(args),
                Stream.of(trace())
        ).toArray();

        log.info(message + " - {}", argsWithTrace);
    }

}
