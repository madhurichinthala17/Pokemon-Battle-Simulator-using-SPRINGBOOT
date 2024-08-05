package com.impact.pokemon.service;

import com.impact.pokemon.model.Pokemon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceTest {

    private PokemonService pokemonService;
    private List<Pokemon> mockPokemons;

    @BeforeEach
    public void setUp() {
        // Initialize the service and mock the list of Pokemons
        pokemonService = new PokemonService();
        mockPokemons = new ArrayList<>();

        Pokemon pokemonA = new Pokemon();
        pokemonA.setNum(1);
        pokemonA.setName("Bulbasaur");
        pokemonA.setType("Grass");
        pokemonA.setHp(200);
        pokemonA.setAttack(150);
        pokemonA.setDefense(100);
        pokemonA.setSpeed(90);

        Pokemon pokemonB = new Pokemon();
        pokemonB.setNum(2);
        pokemonB.setName("Ivysaur");
        pokemonB.setType("Grass");
        pokemonB.setHp(180);
        pokemonB.setAttack(140);
        pokemonB.setDefense(120);
        pokemonB.setSpeed(80);

        mockPokemons.add(pokemonA);
        mockPokemons.add(pokemonB);

        pokemonService.pokemons = mockPokemons;
    }

    @Test
    public void testInit() {
        pokemonService.init();
        assertNotNull(pokemonService.getPokemons());
        assertFalse(pokemonService.getPokemons().isEmpty());
    }

    @Test
    public void testGetPokemons() {
        List<Pokemon> result = pokemonService.getPokemons();
        assertNotNull(result);
        assertEquals(2, result.size()); // As we added two Pokemons in setUp
    }

    @Test
    public void testGetPokemonByNum() {
        Pokemon result = pokemonService.getPokemonByNum(1); // Adjusting to the correct index
        assertNotNull(result);
        assertEquals("Bulbasaur", result.getName());
    }

    @Test
    public void testBattle() {
        Pokemon pokemon1 = pokemonService.getPokemonByNum(1); // Bulbasaur
        Pokemon pokemon2 = pokemonService.getPokemonByNum(2); // Ivysaur

        PokemonService.BattleResult result = pokemonService.battle(pokemon1, pokemon2);
        assertNotNull(result);
        assertTrue(result.getRemainingHp() > 0); // Ensuring one has positive HP
        assertNotNull(result.getWinner());
        assertTrue("Bulbasaur".equals(result.getWinner()) || "Ivysaur".equals(result.getWinner()));
    }

    @Test
    public void testEffectiveness() {
        double effectiveness = pokemonService.getEffectiveness("fire", "grass");
        assertEquals(2.0, effectiveness);

        effectiveness = pokemonService.getEffectiveness("water", "fire");
        assertEquals(2.0, effectiveness);

        effectiveness = pokemonService.getEffectiveness("electric", "electric");
        assertEquals(1.0, effectiveness);
    }
}
