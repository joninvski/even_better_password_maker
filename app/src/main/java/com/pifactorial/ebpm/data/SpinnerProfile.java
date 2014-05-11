package com.pifactorial.ebpm.data;

public class SpinnerProfile {
    private final String a;
    private final String b;

    public SpinnerProfile(String a, String b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public String toString() {
        return this.a + " - " + this.b;
    }
}
