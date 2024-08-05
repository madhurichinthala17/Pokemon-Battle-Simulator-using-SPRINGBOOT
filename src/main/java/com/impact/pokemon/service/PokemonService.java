package com.impact.pokemon.service;

import com.impact.pokemon.model.Pokemon;
import com.impact.pokemon.util.PokemonData;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;

@Service
public class PokemonService {

    protected List<Pokemon> pokemons;

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
                .filter(pokemon -> pokemon.getNum() == num)
                .findFirst()
                .orElse(null);
    }

    //battle code, and pokemon's taking turns when there is still HP balance
    public BattleResult battle(Pokemon pokemon1, Pokemon pokemon2) {
        Random random = new Random();

        int initialHp1 = pokemon1.getHp();
        int initialHp2 = pokemon2.getHp();


        boolean isFirstPokemonsTurn = pokemon1.getSpeed() > pokemon2.getSpeed() ||
                (pokemon1.getSpeed() == pokemon2.getSpeed() && random.nextBoolean());

        while (pokemon1.getHp() > 0 && pokemon2.getHp() > 0) {
            if (isFirstPokemonsTurn) {
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
            isFirstPokemonsTurn = !isFirstPokemonsTurn;
        }

        Pokemon winner = pokemon1.getHp() > 0 ? pokemon1 : pokemon2;

        BattleResult result = new BattleResult(winner.getName(), winner.getHp(), winner.getImage());


        pokemon1.setHp(initialHp1);
        pokemon2.setHp(initialHp2);

        return result;
    }

    private void attack(Pokemon attacker, Pokemon defender) {
        double effectiveness = getEffectiveness(attacker.getType(), defender.getType());
        int damage = (int) (50 * ((double) attacker.getAttack() / defender.getDefense()) * effectiveness);
        defender.setHp(defender.getHp() - damage);
    }

    public double getEffectiveness(String attackType, String defenseType) {
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

    public static class BattleResult {
        private final String winner;
        private final int remainingHp;
        private final String image;

        public BattleResult(String winner, int remainingHp, String image) {
            this.winner = winner;
            this.remainingHp = remainingHp;
            this.image = image;
        }

        public String getWinner() {
            return winner;
        }

        public int getRemainingHp() {
            return remainingHp;
        }

        public String getImage() {
            return image;
        }
    }
}
