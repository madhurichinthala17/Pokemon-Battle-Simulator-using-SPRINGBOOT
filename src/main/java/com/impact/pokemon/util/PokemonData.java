package com.impact.pokemon.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.impact.pokemon.model.Pokemon;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PokemonData {
    private static final Logger logger = LoggerFactory.getLogger(PokemonData.class);
    private static final String FILEPATH = "data/pokemon.csv";

    public static List<Pokemon> loadPokemonData() {
        List<Pokemon> pokemons = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new ClassPathResource(FILEPATH).getInputStream()))) {
            br.readLine(); // Skipping header
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 12) continue; // Handling inconsistencies
                Pokemon pokemon = new Pokemon();
                pokemon.setNum(Integer.parseInt(values[0]));
                pokemon.setName(values[1]);
                pokemon.setType(values[2]);
                //value[3] = Total
                pokemon.setHp(Integer.parseInt(values[4]));
                pokemon.setAttack(Integer.parseInt(values[5]));
                pokemon.setDefense(Integer.parseInt(values[6]));
                //value[7]=SpecialAttack
                //value[8]=SpecialDefense
                pokemon.setSpeed(Integer.parseInt(values[9]));
                //value[10]=Generation
                //value[11]=Legendary
                pokemons.add(pokemon);
            }
        } catch (IOException e) {
            logger.error("Error reading Pokémon data", e);
        } catch (NumberFormatException e) {
            logger.error("Error parsing Pokémon data", e);
        }
        return pokemons;
    }
}
