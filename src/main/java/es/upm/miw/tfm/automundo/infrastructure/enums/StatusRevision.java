package es.upm.miw.tfm.automundo.infrastructure.enums;

public enum StatusRevision {
    POR_CONFIRMAR("Por confirmar", 1),
    EN_MANTENIMIENTO("En mantenimiento", 2),
    NEGADO("Negado", 3),
    FINALIZADO("Finalizado", 4);

    private final String name;
    private final Integer value;

    StatusRevision(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName(){
        return name;
    }

    public Integer getValue(){
        return value;
    }
}
