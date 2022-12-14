package Info;

public class Content {
    public String title;
    public String genre;
    public String year;
    public double rating;


    public Content(String title, String year, String genre, double rating) {
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.rating = rating;
    }

    public String getTitle(){
        return title;
    }
    public String getGenre(){
        return genre;
    }
    public String getYear(){
        return year;
    }
    public double getRating(){
        return rating;
    }
}

