package Info;

public class Series extends Content {

    public String season;

    public Series(String title, String year, String genre, double rating, String season) {
        super(title, year, genre, rating);
        this.season = season;
    }
}
