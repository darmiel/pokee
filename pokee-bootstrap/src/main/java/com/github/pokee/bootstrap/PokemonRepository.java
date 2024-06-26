package com.github.pokee.bootstrap;

import com.github.pokee.common.Pokemon;

import java.util.List;
import java.util.Optional;

public interface PokemonRepository {

    List<Pokemon> findAll();

    Optional<Pokemon> findById(final int id);

}
