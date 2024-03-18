package org.example.plugin.desensitizer;


public enum DesensitizationStrategy {
    USER(s -> s.replaceAll("[\\s]", "$1"))
    ;

    private Desensitizer desensitizer;

    public Desensitizer getDesensitizer() {
        return desensitizer;
    }

    DesensitizationStrategy(Desensitizer d) {
        desensitizer = d;
    }
}
