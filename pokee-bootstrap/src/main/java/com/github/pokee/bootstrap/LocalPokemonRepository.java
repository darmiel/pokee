package com.github.pokee.bootstrap;

import com.github.pokee.common.Pokemon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocalPokemonRepository implements PokemonRepository {

    private final List<Pokemon> pokemonList;

    public LocalPokemonRepository(
            final List<Pokemon> pokemonList
    ) {
        this.pokemonList = pokemonList;
    }

    @Override
    public List<Pokemon> findAll() {
        return new ArrayList<>(this.pokemonList); // this is a bit unoptimized since pokemonList could be immutable by default
    }

    @Override
    public Optional<Pokemon> findById(int id) {
        return this.pokemonList.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

}
