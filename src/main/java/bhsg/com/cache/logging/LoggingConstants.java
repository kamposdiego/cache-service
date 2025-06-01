package bhsg.com.cache.logging;

public final class LoggingConstants {

    private LoggingConstants() {
        // Utility class
    }

    public static final class Tags {
        public static final String SERVICE = "[SERVICE]";
        public static final String INTERCEPTOR = "[INTERCEPTOR]";
        public static final String REDIS = "[REDIS]";
        public static final String IDEMPOTENT = "[IDEMPOTENT]";
        public static final String ERROR = "[ERROR]";
    }

    public static final class Symbols {
        public static final String IN = "→";
        public static final String OUT = "←";
        public static final String IDEMPOTENT_REPLAY = "↻";
        public static final String ERROR = "✖";
        public static final String CLEANUP = "🧹";
    }

}