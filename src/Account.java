import java.util.ArrayList;
import java.util.SortedSet;

public class Account {
    private Information information;
    private ArrayList<Character> characterList;
    private int gamesPlayed;

    public Account(Builder builder) {
        this.characterList = builder.characterList;
        this.gamesPlayed = builder.gamesPlayed;
        this.information = builder.information;
    }

    public Information getInformation() {
        return information;
    }

    public ArrayList<Character> getCharacterList() {
        return characterList;
    }

    public int getGamesPlayed(){
        return gamesPlayed;
    }

    public void incrementGamesPlayed() {
        gamesPlayed++;
    }

    public void setCharacterList(ArrayList<Character> newCharacters) {
        this.characterList = newCharacters;
    }

    public static class Builder{
        private Information information;
        private ArrayList<Character> characterList;
        private int gamesPlayed;

        public Builder information(Information information){
            this.information = information;
            return this;
        }

        public Builder characterList(ArrayList<Character> characterList){
            this.characterList = characterList;
            return this;
        }

        public Builder gamesPlayed(int gamesPlayed){
            this.gamesPlayed = gamesPlayed;
            return this;
        }

        public Account build(){
            return new Account(this);
        }
    }

    public static class Information {
        private Credentials credentials;
        private SortedSet<String> favoriteGames;
        private String name;
        private String country;

        public Information(Builder builder) {
            this.credentials = builder.credentials;
            this.favoriteGames = builder.favoriteGames;
            this.name = builder.name;
            this.country = builder.country;
        }

        public String getName() {
            return name;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        @Override
        public String toString() {
            return "Name: " + name +
                    " Country: " + country + "\n" +
                    " Credentials: " + credentials + "\n" +
                    " Favorite Games: " + favoriteGames + "\n";
        }

        public static class Builder{
            private String name;
            private String country;
            private Credentials credentials;
            private SortedSet<String> favoriteGames;

            public Builder name(String name){
                this.name = name;
                return this;
            }

            public Builder country(String country){
                this.country = country;
                return this;
            }

            public Builder credentials(Credentials credentials){
                this.credentials = credentials;
                return this;
            }

            public Builder favoriteGames(SortedSet<String> favoriteGames){
                this.favoriteGames = favoriteGames;
                return this;
            }

            public Information build(){
                return new Information(this);
            }
        }


    }

    @Override
    public String toString() {
        return "Account: " + information +
                "Games Played: " + gamesPlayed + "\n" +
                "Characters: " + characterList + "\n";
    }
}