package Info;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Stream;

public class Info {
    ArrayList<Content> contentList;
    ArrayList<File> imageFileList;
    static final String movieDataFile = "src/Data/film.txt";
    static final String seriesDataFile = "src/Data/serier.txt";
    static final String moviePosterDirectory = "src/Data/filmplakater";
    static final String seriesPosterDirectory = "src/Data/serieforsider";

    File movieDirectory;    File[] movieFiles;
    File seriesDirectory;   File[] seriesFiles;

    public Info() {
        this.contentList = new ArrayList<>();
        filterAllInfo();
    }

    public void filterAllInfo() {

        this.movieDirectory = new File(moviePosterDirectory);
        this.movieFiles = movieDirectory.listFiles();
        this.seriesDirectory = new File(seriesPosterDirectory);
        this.seriesFiles = seriesDirectory.listFiles();

        //Filter data:
        filterData(contentList, movieDataFile);
        filterData(contentList, seriesDataFile);
        Collections.sort(this.contentList, Comparator.comparing(Content::getTitle));

        //Merge image files:
        mergeImageFiles();
        Collections.sort(this.imageFileList, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) { return (((File) f1).getName().compareTo(((File) f2).getName())); }
        });
        getSize();
        printMirrorFile();
    }

    public void filterData(ArrayList<Content> contentList, String inputDataType) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputDataType));
            String contentLine = reader.readLine();
            while(contentLine != null) {
                String[] words = contentLine.split(";");                //| Splits on the character ';'
                words[3] = words[3].replace(",", ".");       //| Deletes the comma at ...
                words[1] = words[1].trim();                                    //| Trim all white spaces
                Content filteredContent = (inputDataType.equals(movieDataFile)) ? filterMovieData(words) : filterSeriesData(words);
                contentList.add(filteredContent);
                contentLine = reader.readLine();
            }
            reader.close(); // Like 'Break' - stops the BufferedReader.
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    // Make an object that creates a movie with the corresponding index positions. (title, year, genre, rating).
    public Content filterMovieData(String[] words) {
        return new Movies(words[0], words[1], words[2], Double.parseDouble(words[3]));
    }

    public Content filterSeriesData(String[] words) {
        return new Series(words[0], words[1], words[2], Double.parseDouble(words[3]), words[4]);
    }

    // Merge the links to both the 'series' and 'movies' together to make a single ArrayList of type File with all
    // the content inside.
    public void mergeImageFiles() {
        File[] concatenatedList = Stream.concat(Arrays.stream(this.movieFiles), Arrays.stream(this.seriesFiles)).toArray(contents -> (File[]) Array.newInstance(this.movieFiles.getClass().getComponentType(), contents));
        ArrayList<File> retArr = new ArrayList<>();
        Collections.addAll(retArr, concatenatedList);
        this.imageFileList = retArr;
    }

    public ArrayList<Content> getContentList() {
        return this.contentList;
    }

    public ArrayList<File> getFileList() {
        return this.imageFileList;
    }

    public void getSize() {
        System.out.println(this.contentList.size());
    }

    public void printMirrorFile() {
        for(int i = 0; i < this.contentList.size(); i++) {
            System.out.print(this.contentList.get(i).title);
            System.out.print("  |  ");
            System.out.print(this.imageFileList.get(i).getName());
            System.out.println();
        }
    }
}
