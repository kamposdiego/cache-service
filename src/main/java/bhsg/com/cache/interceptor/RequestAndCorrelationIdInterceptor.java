package bhsg.com.cache.interceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static bhsg.com.cache.logging.LoggingConstants.Symbols.CLEANUP;
import static bhsg.com.cache.logging.LoggingConstants.Symbols.OUT;
import static bhsg.com.cache.logging.LoggingConstants.Tags.INTERCEPTOR;

@Slf4j
@Component
public class RequestAndCorrelationIdInterceptor implements ServerInterceptor {

    public static final String REQUEST_ID_HEADER = "X-Request-ID";
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

        Metadata.Key<String> requestIdKey = Metadata.Key.of(REQUEST_ID_HEADER, Metadata.ASCII_STRING_MARSHALLER);
        Metadata.Key<String> correlationIdKey = Metadata.Key.of(CORRELATION_ID_HEADER, Metadata.ASCII_STRING_MARSHALLER);

        final String requestId = Optional.ofNullable(headers.get(requestIdKey)).orElse(UUID.randomUUID().toString());
        final String correlationId = Optional.ofNullable(headers.get(correlationIdKey)).orElse(UUID.randomUUID().toString());

        MDC.put(REQUEST_ID_HEADER, requestId);
        MDC.put(CORRELATION_ID_HEADER, correlationId);

        log.debug("{} {} Initialized trace context - requestId={}, correlationId={}, method={}", OUT, INTERCEPTOR, requestId, correlationId, call.getMethodDescriptor().getFullMethodName());

        ServerCall.Listener<ReqT> listener = next.startCall(call, headers);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(listener) {
            @Override
            public void onComplete() {
                try {
                    super.onComplete();
                } finally {
                    MDC.clear();
                    log.debug("{} {} {} Cleared trace context - requestId={}, correlationId={}", OUT, INTERCEPTOR, CLEANUP, requestId, correlationId);
                }
            }

            @Override
            public void onCancel() {
                try {
                    super.onCancel();
                } finally {
                    MDC.clear();
                    log.debug("{} {} {} Cleared trace context after cancel - requestId={}, correlationId={}", OUT, INTERCEPTOR, CLEANUP, requestId, correlationId);
                }
            }
        };
    }
}

