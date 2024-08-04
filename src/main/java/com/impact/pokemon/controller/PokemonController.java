package com.impact.pokemon.controller;

import  org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.impact.pokemon.model.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import com.impact.pokemon.service.PokemonService;
import com.impact.pokemon.util.PokemonData;
import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@RestController
public class PokemonController {

    private static final Logger logger = LoggerFactory.getLogger(PokemonController.class);

    @Autowired
    private PokemonService pokemonService;


    @GetMapping("/attack")
    public Map<String, Object> attack(int pokemonA, int pokemonB) {
        logger.info("Requested pokemonA: {}, pokemonB: {}", pokemonA, pokemonB);

        Pokemon p1 = pokemonService.getPokemonByNum(pokemonA);
        Pokemon p2 = pokemonService.getPokemonByNum(pokemonB);

        if (p1 == null || p2 == null) {
            return Map.of("error", "Invalid Pokemon number(s)");
        }

        Pokemon winner = pokemonService.battle(p1, p2);

        return Map.of(
                "winner", winner.getName(),
                "hitPoints", winner.getHp(),
                "image", winner.getImage()
        );

    }
}




