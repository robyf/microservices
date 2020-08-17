package net.robyf.ms.lending.persistence;

public enum EventType {

    CREDIT_DECISION_ACCEPTED(1000, 1);

    private int code;
    private int monetaryImpact;

    EventType(final int code, final int monetaryImpact) {
        this.code = code;
        this.monetaryImpact = monetaryImpact;
    }

    public int getCode() {
        return code;
    }

    public int getMonetaryImpact() {
        return monetaryImpact;
    }

}
