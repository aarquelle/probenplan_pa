package org.aarquelle.probenplan_pa.data.exception;

public class DBNotAvailableException extends RuntimeException {
    public DBNotAvailableException(Throwable e) {
        super(e);
    }
}
