package com.impact.pokemon.controller;

import com.impact.pokemon.model.Pokemon;
import com.impact.pokemon.service.PokemonService;
import com.impact.pokemon.service.PokemonService.BattleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PokemonController {

    private static final Logger logger = LoggerFactory.getLogger(PokemonController.class);

    @Autowired
    private PokemonService pokemonService;

    @GetMapping("/attack")
    public ResponseEntity<Map<String, Object>> attack(@RequestParam int pokemonA, @RequestParam int pokemonB) {
        logger.info("Requested pokemonA: {}, pokemonB: {}", pokemonA, pokemonB);

        Pokemon p1 = pokemonService.getPokemonByNum(pokemonA);
        Pokemon p2 = pokemonService.getPokemonByNum(pokemonB);

        //If user havent entered any pokemon, returning NOTFOUND status
        if (p1 == null || p2 == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Invalid Pokemon number(s)"));
        }

        //If both pokemons are same, not continuing with battle --> setting response to BadRequest
        if(p1==p2){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Battle cannot be initiated with two identical Pokémon numbers."));
        }

        //else battle
        BattleResult result = pokemonService.battle(p1, p2);

        return ResponseEntity.ok(Map.of(
                "winner", result.getWinner(),
                "hitPoints", result.getRemainingHp(),
                "image", result.getImage()
        ));
    }

    @GetMapping("/pokemon")
    public ResponseEntity<Map<String, Object>> getPokemonDetails(@RequestParam int number) {
        Pokemon pokemon = pokemonService.getPokemonByNum(number);
        if (pokemon == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Invalid Pokemon number"));
        }
        return ResponseEntity.ok(Map.of(
                "number", pokemon.getNum(),
                "name", pokemon.getName(),
                "type", pokemon.getType(),
                "hp", pokemon.getHp(),
                "attack", pokemon.getAttack(),
                "defense", pokemon.getDefense(),
                "speed", pokemon.getSpeed(),
                "image", pokemon.getImage()
        ));
    }

}
