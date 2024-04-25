package com.github.pokee.common;


import com.github.pokee.common.fielder.Fielder;
import com.github.pokee.pson.mapper.annotations.JsonProperty;
import com.github.pokee.pson.mapper.annotations.PsonSource;

@PsonSource(file = "pokemon.json")
public class Pokemon implements Fielder {

    @JsonProperty("hp")
    final int hp;
    @JsonProperty("attack")
    final int attack;
    @JsonProperty("defense")
    final int defense;
    @JsonProperty("speed")
    final int speed;
    @JsonProperty("pokedexID")
    private final int id;
    @JsonProperty("name")
    private final LocalizedString name;
    @JsonProperty("description")
    private final LocalizedString description;

    public Pokemon(final int id,
                   final LocalizedString name,
                   final LocalizedString description,
                   final int hp,
                   final int attack,
                   final int defense,
                   final int speed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", name=" + name +
                ", description=" + description +
                ", hp=" + hp +
                ", attack=" + attack +
                ", defense=" + defense +
                ", speed=" + speed +
                '}';
    }

    @Override
    public Object getField(final String name) {
        return switch (name.toLowerCase()) {
            case "id" -> this.id;
            case "name" -> this.name;
            case "description" -> this.description;
            case "hp" -> this.hp;
            case "attack" -> this.attack;
            case "defense" -> this.defense;
            case "speed" -> this.speed;
            default -> throw new IllegalArgumentException("Field " + name + " not found");
        };
    }

    @Override
    public String[] getFields() {
        return new String[]{"id", "name", "description", "hp", "attack", "defense", "speed"};
    }

}
