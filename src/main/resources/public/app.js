const app = Vue.createApp({
    data() {
        return {
            showWelcome: true,
            firstNum: null,
            secondNum: null,
            result: '',
            errorMessage: '',
            showDetails: false,
            winner: null,
            pokemon1Details: {},
            pokemon2Details: {}
        };
    },
    mounted() {
        // Hide the welcome message after 3 seconds
        setTimeout(() => {
            this.showWelcome = false;
        }, 3000);
    },
    methods: {
        fetchPokemonDetails() {
            this.showDetails = false;
            this.winner = null;

            // Fetch details of both Pokémon
            Promise.all([
                fetch(`/pokemon?number=${this.firstNum}`).then(response => response.json()),
                fetch(`/pokemon?number=${this.secondNum}`).then(response => response.json())
            ]).then(([pokemon1Data, pokemon2Data]) => {
                this.pokemon1Details = pokemon1Data;
                this.pokemon2Details = pokemon2Data;
                this.showDetails = true;

                // Wait for 15 seconds before displaying the winner
                setTimeout(this.battle, 200);
            });
        },
        battle() {
            if (this.firstNum == null || this.secondNum == null) {
                this.errorMessage = "Please enter valid numbers for both Pokemon.";
                return;
            }
            if (this.firstNum < 1 || this.secondNum < 1 || this.firstNum > 493 || this.secondNum > 493) {
                this.errorMessage = "Please enter valid numbers for both Pokemon.";
                return;
            }
            if(this.firstNum == this.secondNum){
                this.errorMessage="Both pokemons can't be same!!!"
                return;
            }


            fetch(`/attack?pokemonA=${this.firstNum}&pokemonB=${this.secondNum}`)
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        this.errorMessage = data.error;
                        this.result = '';
                        this.winner = null;
                    } else {
                        this.result = `${data.winner} wins with ${data.hitPoints} HP remaining!`;
                        this.winner = {
                            name: data.winner,
                            image: data.image,
                            hp: data.hitPoints
                        };
                        this.errorMessage = '';
                    }
                });
        }
    }
});

app.mount('#app');
