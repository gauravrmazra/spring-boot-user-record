package in.blogspot.javawithgaurav.utility;

import java.util.Objects;

public class RestPreConditions {
    public static <T> void requireNonNull(T t) {
        if (Objects.isNull(t)) {
            throw new RestPreConditionViolationException("Rest condition violated");
        }
    }
    
    public static <T> void requireNull(T t) {
        if (!Objects.isNull(t)) {
            throw new RestPreConditionViolationException("Rest condition violated");
        }
    }

    static class RestPreConditionViolationException extends RuntimeException {

        private static final long serialVersionUID = -8462956983559931125L;

        public RestPreConditionViolationException() {

        }

        public RestPreConditionViolationException(final String message) {
            super(message);
        }
    }
}
