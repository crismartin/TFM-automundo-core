package es.upm.miw.tfm.automundo.infrastructure.enums;

public enum StatusRevision {
    POR_CONFIRMAR("Por confirmar"),
    EN_MANTENIMIENTO("En mantenimiento"),
    NEGADO("Negado"),
    FINALIZADO("Finalizado"),
    NULO(null);

    private final String name;

    StatusRevision(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
