package ua.kpi.fict.carpark.exception;

public class BusNotFoundException extends RuntimeException {

    public BusNotFoundException(String message) {
        super(message);
    }
}
