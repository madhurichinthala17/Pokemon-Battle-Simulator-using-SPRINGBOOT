package com.impact.pokemon.controller;

import com.impact.pokemon.model.Pokemon;
import com.impact.pokemon.service.PokemonService;
import com.impact.pokemon.service.PokemonService.BattleResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PokemonControllerTest {

    private TestRestTemplate rest;

    @LocalServerPort
    private int port;

    @Mock
    private PokemonService pokemonService;

    @InjectMocks
    private PokemonController pokemonController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rest = new TestRestTemplate(new RestTemplateBuilder().rootUri(format("http://localhost:%d", port)));
    }

    //testing if winner is correct
    @Test
    void testAttackPicksWinnerWithHitPoints() {
        Pokemon pokemonA = new Pokemon();
        pokemonA.setNum(1);
        pokemonA.setName("Bulbasaur");
        pokemonA.setType("Grass");
        pokemonA.setHp(200);
        pokemonA.setAttack(150);
        pokemonA.setDefense(100);
        pokemonA.setSpeed(90);
        //pokemonA.setImage("bulbasaur.png");

        Pokemon pokemonB = new Pokemon();
        pokemonB.setNum(2);
        pokemonB.setName("Ivysaur");
        pokemonB.setType("Grass");
        pokemonB.setHp(180);
        pokemonB.setAttack(140);
        pokemonB.setDefense(120);
        pokemonB.setSpeed(80);

        BattleResult result = new BattleResult("Ivysaur", 56, "https://img.pokemondb.net/sprites/home/normal/" + "Ivysaur".toLowerCase() + ".png");

        when(pokemonService.getPokemonByNum(1)).thenReturn(pokemonA);
        when(pokemonService.getPokemonByNum(4)).thenReturn(pokemonB);
        when(pokemonService.battle(pokemonA, pokemonB)).thenReturn(result);

        Map<String, Object> response = rest.getForObject("/attack?pokemonA=1&pokemonB=2", Map.class);

        assertEquals("Ivysaur", response.get("winner"));
        assertEquals(56, response.get("hitPoints"));
        assertEquals("https://img.pokemondb.net/sprites/home/normal/" + "Ivysaur".toLowerCase() + ".png", response.get("image"));
    }

    //testing with Invalid Pokemon Number
    @Test
    void testAttackWithInvalidPokemonNumber() {
        when(pokemonService.getPokemonByNum(999)).thenReturn(null);

        ResponseEntity<Map> response = rest.getForEntity("/attack?pokemonA=999&pokemonB=2", Map.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
    }

    //Testing with Valid Number
    @Test
    void testGetPokemonDetailsWithValidNumber() {
        Pokemon pikachu = new Pokemon();
        pikachu.setNum(25);
        pikachu.setName("Pikachu");
        pikachu.setType("Electric");
        pikachu.setHp(35);
        pikachu.setAttack(55);
        pikachu.setDefense(40);
        pikachu.setSpeed(90);
        //pikachu.setImage("pikachu.png");

        when(pokemonService.getPokemonByNum(25)).thenReturn(pikachu);



        ResponseEntity<Map<String, Object>> response = rest.getForEntity("/pokemon?number=25", (Class<Map<String, Object>>) (Class<?>) Map.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertEquals("Pikachu", responseBody.get("name"));
        assertEquals("Electric", responseBody.get("type"));
        assertEquals(35, responseBody.get("hp"));
        assertEquals(55, responseBody.get("attack"));
        assertEquals(40, responseBody.get("defense"));
        assertEquals(90, responseBody.get("speed"));
        //assertEquals("pikachu.png", responseBody.get("image"));
    }

    //testing with Same Numbers
    @Test
    void testWithSameNumbers(){
        Pokemon pikachu = new Pokemon();
        pikachu.setNum(25);
        pikachu.setName("Pikachu");
        pikachu.setType("Electric");
        pikachu.setHp(35);
        pikachu.setAttack(55);
        pikachu.setDefense(40);
        pikachu.setSpeed(90);

        when(pokemonService.getPokemonByNum(25)).thenReturn(pikachu);
        ResponseEntity<Map> response = rest.getForEntity("/attack?pokemonA=25&pokemonB=25", Map.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));

    }

    //testing with Invalid Numbers
    @Test
    void testGetPokemonDetailsWithInvalidNumber() {

        when(pokemonService.getPokemonByNum(999)).thenReturn(null);
        ResponseEntity<Map<String, Object>> response = rest.getForEntity("/pokemon?number=999", (Class<Map<String, Object>>) (Class<?>) Map.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Invalid Pokemon number", response.getBody().get("error"));
    }

}