package de.dhbwravensburg.etfadvisor.exceptions;

public class EtfNotFoundException extends RuntimeException {

    public EtfNotFoundException(Long etfId) {
        super("Etf with"+ etfId + "id, not found");
    }

    public EtfNotFoundException(String searchingBy) {
        super("Etf with"+ searchingBy + ", not found");
    }
}
