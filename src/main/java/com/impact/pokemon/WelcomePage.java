package com.impact.pokemon;

import org.springframework.stereotype.Component;

@Component
public class WelcomePage {

    public String getWelcomeMessage(){
        return "Welcome to the BATTLE!!";
    }
}