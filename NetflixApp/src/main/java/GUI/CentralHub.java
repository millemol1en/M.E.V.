package GUI;

import Info.Content;
import Info.KMP;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class CentralHub {
    // Data:
    ArrayList<Content>  contentList;
    ArrayList<File>     posterImages;
    ArrayList<Content>  favouritesData;  ArrayList<File> favouritesImages;
    ArrayList<Content>  searchedData;    ArrayList<File> searchedImages;
    Content             globalTarget;

    // Layers:
    BorderPane          homeRoot;
    StackPane           homeStackPane;
    FlowPane            homeFlowPane;
    ScrollPane          homeScrollPane;
    HBox                homeMenuBar;
    InformationPanel    infoPanel;
    FrostPanel          frostPanel;

    // Effect:
    DropShadow imgDropShadow;
    DropShadow menuBarEffect;


    // Central hub constructor:
    CentralHub(ArrayList<Content> inputContentList, ArrayList<File> inputPosterImages, Stage stage) {
        allocateData(inputContentList, inputPosterImages);
        homePage(stage);
    }

    /****************************************

                    HOME PAGE

     *****************************************/
    public void homePage(Stage stageRef) {
        // Initiailizing components:
        this.infoPanel      = new InformationPanel(this.contentList, this.posterImages, this.favouritesData, this.favouritesImages);
        this.homeRoot       = new BorderPane();
        this.homeStackPane  = new StackPane();
        this.homeFlowPane   = new FlowPane();
        this.frostPanel     = new FrostPanel(stageRef, this.infoPanel);
        this.homeScrollPane = new ScrollPane(this.homeFlowPane);
        this.imgDropShadow  = new DropShadow();
        this.menuBarEffect  = new DropShadow();
        this.homeMenuBar    = menuBar();

        // Styling, loading image and gluing together layers:
        styleComponents(this.homeRoot, this.homeFlowPane, this.homeScrollPane, this.homeStackPane, this.imgDropShadow, this.menuBarEffect);
        loadImages(contentList, posterImages, this.homeFlowPane);
        sandwichLayers(this.homeRoot, this.homeStackPane, this.homeScrollPane, this.infoPanel, this.frostPanel, this.homeMenuBar);
    }

    /****************************************

                LOAD IMAGES

     *****************************************/
    public void loadImages(ArrayList<Content> contentList, ArrayList<File> fileList, FlowPane inputFlowPane) {
        ImageView[] imageViewArray = new ImageView[contentList.size()];
        Image[] imageArray = new Image[contentList.size()];

        for (int i = 0; i < contentList.size(); i++) {
            try{
                // Base Info:
                imageArray[i] = new Image(fileList.get(i).toURI().toString());
                imageViewArray[i] = new ImageView(imageArray[i]);
                String titleName = fileList.get(i).getAbsoluteFile().getName();

                // Branding:
                imageViewArray[i].setUserData(titleName.replace(".jpg", ""));
                imageViewArray[i].setId("" + i);

                // Effects:
                var pictureHover = new ScaleTransition(Duration.millis(250), imageViewArray[i]);
                pictureHover.setFromX(1.0);             // start of X
                pictureHover.setFromY(1.0);             // start of Y
                pictureHover.setToX(1.2);               // increase X by 20%
                pictureHover.setToY(1.2);               // increase Y by 20%
                imageViewArray[i].setEffect(imgDropShadow);

                //On-HOVER event listener:
                imageViewArray[i].setOnMouseEntered((evtEnter) -> {
                    Node targetPicture = (Node) evtEnter.getSource();
                    pictureHover.setRate(1.5);
                    targetPicture.setViewOrder(-1.0);
                    pictureHover.play();
                });
                /*ON HOVER EVENT - mouse EXIT*/
                imageViewArray[i].setOnMouseExited((evtExit) -> {
                    Node targetPicture = (Node) evtExit.getSource();
                    pictureHover.setRate(-1.0);
                    targetPicture.setViewOrder(0.0);
                    pictureHover.play();
                });

                //On-CLICK event listener:
                imageViewArray[i].setOnMouseClicked((evt) -> {
                    System.out.println("Size: " + this.favouritesData.size());
                    Node targetPictureSource = (Node) evt.getSource();
                    String targetImageName = imageViewArray[Integer.parseInt(targetPictureSource.getId())].getUserData().toString();
                    this.infoPanel.setTargetImage(retImage(targetImageName, fileList));

                    for(Content info : contentList) {
                        if(info.title.equals(targetImageName)) {
                            this.globalTarget = info;                       //| SET GLOBAL TARGET
                            this.infoPanel.setPotentialTarget(info);        //| SET POTENTIAL TARGET
                            Field[] fields = info.getClass().getFields();
                            for(Field field : fields) {
                                try {
                                    this.infoPanel.setNewItem(field.get(info).toString());
                                } catch(IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    // Make the info panel and frost panel both visible
                    this.infoPanel.setVisible();
                    this.frostPanel.setVisible();
                });
                inputFlowPane.getChildren().add(imageViewArray[i]);

            } catch (Exception e) {
                System.out.println("No more space");
            }
        }
    }

    /***********************************************

     COMPONENT SANDWICHING, STYLING & DATA HANDLING

     **********************************************/
    public void allocateData(ArrayList<Content> inputContentList, ArrayList<File> inputPosterImages) {
        this.contentList    = inputContentList;
        this.posterImages   = inputPosterImages;
        this.favouritesData = new ArrayList<>(); this.favouritesImages = new ArrayList<>();
        this.searchedData   = new ArrayList<>(); this.searchedImages   = new ArrayList<>();
    }

    // We add 'input' to the name of the nodes that we pass in, so we know what it is that we grab from somewhere else.
    public void sandwichLayers(BorderPane inputBorderPane, StackPane inputStackPane, ScrollPane inputScrollPane, InformationPanel inputInfoPanel, FrostPanel inputFrostPanel, HBox inputHomeMenuBar) {
        inputStackPane.getChildren().add(inputScrollPane);
        inputStackPane.getChildren().add(inputFrostPanel.getRoot());
        inputStackPane.getChildren().add(inputInfoPanel.getRoot());
        inputBorderPane.setCenter(inputStackPane);
        // Adds menuBar to the top BorderPane;
        inputBorderPane.setTop(inputHomeMenuBar);

    }

    public void styleComponents(BorderPane root, FlowPane flowPane, ScrollPane scrollPane, StackPane stackPane, DropShadow imgDropShadow, DropShadow menuBarEffect) {
        root.setStyle("-fx-background-color: #430856;");

        stackPane.setStyle("-fx-background-color: #430856;");

        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: #430856;");

        flowPane.setAlignment(Pos.CENTER);
        flowPane.setHgap(20);
        flowPane.setVgap(25);
        flowPane.setPrefWrapLength(1350);
        flowPane.setStyle("-fx-background-color: #430856; -fx-padding: 38 0 125 0;");

        imgDropShadow.setBlurType(BlurType.GAUSSIAN);
        imgDropShadow.setColor(Color.WHITE);
        imgDropShadow.setHeight(20);
        imgDropShadow.setWidth(20);

        menuBarEffect.setBlurType(BlurType.GAUSSIAN);
        menuBarEffect.setColor(Color.DARKGREY);
        menuBarEffect.setSpread(0.3);
        menuBarEffect.setHeight(10);
        menuBarEffect.setWidth(10);
    }

    public ImageView retImage(String target, ArrayList<File> filesList) {
        Image targetImage;
        ImageView imageToReturn = null;
        for(int i = 0; i < filesList.size(); i++) {
            if(filesList.get(i).getAbsoluteFile().getName().replace(".jpg", "").equals(target)) {
                // Return image...
                targetImage = new Image(filesList.get(i).toURI().toString());
                imageToReturn = new ImageView(targetImage);
                imageToReturn.setScaleY(1.2);
                imageToReturn.setScaleX(1.2);
                imageToReturn.setEffect(imgDropShadow);
            }
        }
        return imageToReturn;
    }

    /***********************************

                  MENU

     ***********************************/
    public HBox menuBar() {
        // Init components:
        HBox menuBar = new HBox();

        TextField searchBar = new TextField();

        Button homePageBtn = new Button("Home");
        Button favouritesPageBtn = new Button("View Favourites");
        Button searchButton = new Button();

        File iconFile = new File("src/Data/icons/icon.png");
        Image searchIconData = new Image(iconFile.toURI().toString());
        ImageView searchIcon = new ImageView(searchIconData);
        searchIcon.setFitHeight(24);
        searchIcon.setFitWidth(24);
        searchIcon.setViewOrder(2.0);
        searchIcon.setCursor(Cursor.HAND);
        searchIcon.setOnMouseClicked((evtClick) -> {
            System.out.println("Picture clicked!");
            this.searchedData.clear();
            this.searchedImages.clear();
            searchAlgorithm(searchBar);
        });

        AnchorPane joinSearchBarAndSymbol = new AnchorPane();
        AnchorPane.setLeftAnchor(searchBar, 1.0);
        AnchorPane.setRightAnchor(searchIcon, 12.0);
        AnchorPane.setTopAnchor(searchIcon, 11.0);

        // Sandwich together components:
        joinSearchBarAndSymbol.getChildren().addAll(searchBar, searchIcon);
        menuBar.getChildren().addAll(homePageBtn, favouritesPageBtn, joinSearchBarAndSymbol);

        // Button button-icon:
        searchButton.setCursor(Cursor.HAND);
        searchButton.setStyle("-fx-background-color: none");
        searchButton.setGraphic(searchIcon);
        searchButton.setOnMouseClicked((evtClicked) -> {
            this.searchedData.clear();
            this.searchedImages.clear();
            searchAlgorithm(searchBar);
        });

        // Home Button:
        homePageBtn.setStyle("-fx-background-color: #340a48; -fx-padding: 10; -fx-font-family: 'Damascus'; -fx-text-fill: #ffff; -fx-border-width: 1.5 1.5 3 1.5; -fx-border-color: #ffff; -fx-font-size: 18px");
        homePageBtn.setOnMouseClicked((evtClick -> {
            homePageBtn.setStyle("-fx-background-color: #340a48; -fx-padding: 10; -fx-font-family: 'Damascus'; -fx-text-fill: #ffff; -fx-border-width: 1.5 1.5 3 1.5; -fx-border-color: #ffff; -fx-font-size: 18px");
            favouritesPageBtn.setStyle("-fx-background-color: #560b75; -fx-padding: 10; -fx-font-family: 'Damascus'; -fx-text-fill: #ffff; -fx-border-width: 1.5 1.5 1.5 1.5; -fx-border-color: #ffff; -fx-font-size: 18px");
            this.searchedData.clear();
            this.searchedImages.clear();
            this.homeFlowPane.getChildren().clear();
            loadImages(this.contentList, this.posterImages, this.homeFlowPane);
        }));

        // Favourites Button:
        favouritesPageBtn.setStyle("-fx-background-color: #560b75; -fx-padding: 10; -fx-font-family: 'Damascus'; -fx-text-fill: #ffff; -fx-border-width: 1.5 1.5 1.5 1.5; -fx-border-color: #ffff; -fx-font-size: 18px");
        favouritesPageBtn.setOnMouseClicked((evtClick -> {
            homePageBtn.setStyle("-fx-background-color: #560b75; -fx-padding: 10; -fx-font-family: 'Damascus'; -fx-text-fill: #ffff; -fx-border-width: 1.5 1.5 1.5 1.5; -fx-border-color: #ffff; -fx-font-size: 18px");
            favouritesPageBtn.setStyle("-fx-background-color: #340a48; -fx-padding: 10; -fx-font-family: 'Damascus'; -fx-text-fill: #ffff; -fx-border-width: 1.5 1.5 3 1.5; -fx-border-color: #ffff; -fx-font-size: 18px");
            this.searchedImages.clear();
            this.homeFlowPane.getChildren().clear();
            loadImages(this.favouritesData, this.favouritesImages, this.homeFlowPane);
        }));

        // Search Bar:
        searchBar.setStyle("-fx-background-color: none; -fx-padding: 10; -fx-font-family: 'Damascus'; -fx-border-width: 1.5 1.5 1.5 1.5; -fx-border-radius: 50px; -fx-border-color: #ffff; -fx-text-fill: #ffff; -fx-font-size: 18px");
        searchBar.setViewOrder(0.0);
        searchBar.setPromptText("Search here...");
        searchBar.setTooltip(new Tooltip("Search for title or genre"));

        searchBar.setOnKeyPressed((keyEvent) -> {
            if(keyEvent.getCode() == KeyCode.ENTER) {
                this.searchedImages.clear();
                this.searchedData.clear();
                searchAlgorithm(searchBar);
                //printListTest();
            }
        });

        // Style Menubar:
        menuBar.setStyle("-fx-background-color: #560b75; -fx-padding: 10 0 10 42");
        menuBar.setSpacing(12);
        menuBar.setEffect(menuBarEffect);

        return menuBar;
    }

    /***********************************

                SEARCH ALGORITHM

     ***********************************/
    public void searchAlgorithm(TextField searchBar) {
        if(searchBar.getText().length() == 0) {
            System.out.println("NO input detected!");
        } else {
            KMP kmp = new KMP();
            for(Content content : contentList) {
                String filteredGenres = content.genre.replace(",", "");
                String inputText = content.title.concat(filteredGenres).toLowerCase().replace(" ","");
                if(kmp.initKMP(inputText, searchBar.getText().toLowerCase().replace(" ", ""))) {
                    this.searchedData.add(content);
                    this.searchedImages.add(posterImages.get(contentList.indexOf(content)));
                }
                this.homeFlowPane.getChildren().clear();
                loadImages(this.searchedData, this.searchedImages, this.homeFlowPane);
            }
        }
    }


    // Get root
    public BorderPane getHomeRoot() {
        return this.homeRoot;
    }
}
