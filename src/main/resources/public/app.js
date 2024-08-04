const app = Vue.createApp({
    setup() {
        // Reactive references
        let firstNum = Vue.ref(null);
        let secondNum = Vue.ref(null);
        let pokemonName = Vue.ref('');
        let errorMessage = Vue.ref('');

        // Battle function with input validation
        function battle() {
            // Clear previous error message
            errorMessage.value = '';

            // Convert refs to regular variables
            const numA = firstNum.value;
            const numB = secondNum.value;

            // Validate numbers
            if (numA < 1 || numA > 493 || numB < 1 || numB > 493) {
                errorMessage.value = 'Numbers must be between 1 and 493!';
                return;
            }
            if (numA == null || numB == null) {
                errorMessage.value = 'Please enter both numbers!';
                return;
            }
            if(numA == numB)
            {
                errorMessage.value='Both Pokemons are same';
                return
            }

            // Fetch battle results
            fetch(`/attack?pokemonA=${numA}&pokemonB=${numB}`)
                .then(response => response.json())
                .then(data => {
                    pokemonName.value = data.winner;
                    console.log(`This Pokémon has ${data.hitPoints} hit points.`);
                })
                .catch(error => {
                    errorMessage.value = 'Error fetching battle data!';
                    console.error('Error:', error);
                });
        }

        // Return refs and methods to the template
        return {
            firstNum,
            secondNum,
            pokemonName,
            errorMessage,
            battle,
        }
    }
});

app.mount('#app');
