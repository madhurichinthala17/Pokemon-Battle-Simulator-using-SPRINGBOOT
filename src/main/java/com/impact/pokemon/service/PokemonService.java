
package com.impact.pokemon.service;

import com.impact.pokemon.model.Pokemon;
import com.impact.pokemon.util.PokemonData;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class PokemonService {
    private List<Pokemon> pokemons;

    @PostConstruct
    public void init() {
        pokemons = PokemonData.loadPokemonData();
        assignImages();
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public Pokemon getPokemonByNum(int num) {
        return pokemons.stream()
                .filter(pokemon -> pokemon.getNum()==num)
                .findFirst()
                .orElse(null);
    }

    public Pokemon battle(Pokemon pokemon1, Pokemon pokemon2) {
        while (pokemon1.getHp() > 0 && pokemon2.getHp() > 0) {
            if (pokemon1.getSpeed() > pokemon2.getSpeed()) {
                attack(pokemon1, pokemon2);
                if (pokemon2.getHp() > 0) {
                    attack(pokemon2, pokemon1);
                }
            } else {
                attack(pokemon2, pokemon1);
                if (pokemon1.getHp() > 0) {
                    attack(pokemon1, pokemon2);
                }
            }
        }
        return pokemon1.getHp() > 0 ? pokemon1 : pokemon2;
    }

    private void attack(Pokemon attacker, Pokemon defender) {
        double effectiveness = getEffectiveness(attacker.getType(), defender.getType());
        int damage = (int) (50 * ((double) attacker.getAttack() / defender.getDefense()) * effectiveness);
        defender.setHp(defender.getHp() - damage);
    }

    private double getEffectiveness(String attackType, String defenseType) {
        switch (attackType.toLowerCase()) {
            case "fire":
                if (defenseType.equalsIgnoreCase("grass")) return 2.0;
                if (defenseType.equalsIgnoreCase("water")) return 0.5;
                break;
            case "water":
                if (defenseType.equalsIgnoreCase("fire")) return 2.0;
                if (defenseType.equalsIgnoreCase("electric")) return 0.5;
                break;
            case "grass":
                if (defenseType.equalsIgnoreCase("electric")) return 2.0;
                if (defenseType.equalsIgnoreCase("fire")) return 0.5;
                break;
            case "electric":
                if (defenseType.equalsIgnoreCase("water")) return 2.0;
                if (defenseType.equalsIgnoreCase("grass")) return 0.5;
                break;
        }
        return 1.0;
    }
    private void assignImages() {
        for (Pokemon pokemon : pokemons) {
            String imageUrl = "https://img.pokemondb.net/sprites/home/normal/" + pokemon.getName().toLowerCase() + ".png";
            pokemon.setImage(imageUrl);
        }
    }
}
