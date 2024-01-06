package org.example.plugin.desensitizer;


import lombok.Getter;

public enum DesensitizationStrategy {
    USER(s -> s.replaceAll("[\\s]", "$1"))
    ;

    @Getter
    private Desensitizer desensitizer;

    DesensitizationStrategy(Desensitizer d) {
        desensitizer = d;
    }
}
