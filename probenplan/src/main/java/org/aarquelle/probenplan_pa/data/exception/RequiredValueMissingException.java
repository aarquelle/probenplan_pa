package org.aarquelle.probenplan_pa.data.exception;

public class RequiredValueMissingException extends Exception {
    public RequiredValueMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequiredValueMissingException(String message) {
        super(message);
    }
}
