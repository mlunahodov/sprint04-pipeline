package io.github.Guimaraes131.vroom.motorcycle.enums;

import lombok.Getter;

@Getter
public enum ProblemCategory {
    MECHANICAL("rgb(255, 0, 0)", "Mecânico"),
    ELECTRICAL("rgb(0, 0, 255)", "Elétrico"),
    DOCUMENTATION("rgb(0, 255, 0)", "Documentação"),
    AESTHETIC("rgb(255, 255, 0)", "Estético"),
    SAFETY("rgb(255, 165, 0)", "Segurança"),
    MULTIPLE("rgb(255, 192, 203)", "Múltiplos"),
    COMPLIANT("rgb(255, 255, 255)", "Conforme");

    private final String associatedColor;
    private final String value;

    ProblemCategory(String color, String value) {
        this.associatedColor = color;
        this.value = value;
    }
}
