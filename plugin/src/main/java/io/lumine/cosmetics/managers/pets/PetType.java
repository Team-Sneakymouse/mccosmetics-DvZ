package io.lumine.cosmetics.managers.pets;

public enum PetType {
    MEG,
    MCPETS;

    public static PetType parse(String value) {
        for(var type : values()) {
            if(type.toString().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return MCPETS;
    }
}
