package GUI;

import Info.Content;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;

public class InformationPanel {
    // Grid containers to categorize the content into specific containers:
    GridPane root;
    GridPane imageAndButton;

    // List all of our movie data:
    ListView contentInfo;
    VBox contentInfoPanels;

    // The image we display:
    ImageView targetImage;

    // Each image we click on is declared as a "potential target", whereby we
    // are able to "add" or "remove" this potential target to our favourites
    Content potentialTarget;

    // Buttons:
    HBox buttons;
    Button add;
    Button remove;

    // Effects:
    FadeTransition fade;
    DropShadow shineEffect;

    // Referenced:
    ArrayList<Content> contentListRef;
    ArrayList<Content> favouriteListRef;
    ArrayList<File> posterDataRef;
    ArrayList<File> favouritesPosterDataRef;

    InformationPanel(ArrayList<Content> inputContentList, ArrayList<File> inputPosterData, ArrayList<Content> inputFavouriteList, ArrayList<File> inputFavouritesPosterData) {
        // Init components:
        this.root = new GridPane();
        this.imageAndButton = new GridPane();
        this.contentInfo = new ListView();
        this.buttons = new HBox();
        this.add = new Button("Add to Favourites");
        this.remove = new Button("Remove from Favourites");
        this.potentialTarget = null;
        this.fade = new FadeTransition(Duration.millis(300), this.root);
        this.shineEffect = new DropShadow();

        // References:
        this.contentListRef = inputContentList;
        this.favouriteListRef = inputFavouriteList;
        this.posterDataRef = inputPosterData;
        this.favouritesPosterDataRef = inputFavouritesPosterData;

        // Styling and sandwiching:
        stylingAndConstraints();
        sandwichLayers();
    }

    public void stylingAndConstraints() {
        // GRID. 01:
        // Constraints to grid. 01:
        ColumnConstraints rootContentCol = new ColumnConstraints();
        rootContentCol.setPercentWidth(50);
        ColumnConstraints rootPictureCol = new ColumnConstraints();
        rootPictureCol.setPercentWidth(50);

        // Styling to grid. 01:
        this.root.getColumnConstraints().addAll(rootContentCol, rootPictureCol);
        this.root.setStyle("-fx-background-color: #360736; -fx-border-width: 3 3 3 3; -fx-border-color: #ffff;");
        this.root.setMaxWidth(850);
        this.root.setMaxHeight(300);
        this.root.setAlignment(Pos.CENTER);
        this.root.setVisible(false);

        // GRID. 02:
        // Constraints to grid. 02:
        RowConstraints pictureRow = new RowConstraints();
        pictureRow.setPercentHeight(90);
        RowConstraints buttonRow = new RowConstraints();
        buttonRow.setPercentHeight(10);

        // Styling to grid. 02:
        this.imageAndButton.setStyle("-fx-padding: 0 0 10 0");
        this.imageAndButton.getRowConstraints().addAll(pictureRow, buttonRow);
        this.imageAndButton.setAlignment(Pos.CENTER);

        // Buttons:
        this.buttons.setSpacing(8);
        File buttonCSS = new File("src/main/resources/css/buttonanimation.css");
        if(buttonCSS == null) {
            System.out.println("Error occurred. Abort!");
            System.exit(-1);
        } else {
            this.add.getStylesheets().add(buttonCSS.toURI().toString());
            this.remove.getStylesheets().add(buttonCSS.toURI().toString());
        }

        this.add.setOnMouseClicked((evt) -> {
            if(this.favouriteListRef.contains(this.potentialTarget)) {
                System.out.println("Title is already there...");
                // Add panel???
            } else {
                System.out.println("Added to list");
                favouriteListRef.add(this.potentialTarget);
                favouritesPosterDataRef.add(this.posterDataRef.get(this.contentListRef.indexOf(this.potentialTarget)));
            }
        });
        this.remove.setOnMouseClicked((evt) -> {
            if(this.favouriteListRef.contains(this.potentialTarget)) {
                favouriteListRef.remove(this.potentialTarget);
                favouritesPosterDataRef.remove(this.posterDataRef.get(this.contentListRef.indexOf(this.potentialTarget)));
            }
            System.out.println("Remove new title...");
        });

        // Content info styling:
        contentInfo.setFocusTraversable(false);
        contentInfo.setMouseTransparent(true);
        File listCSS = new File("src/main/resources/css/liststyle.css");
        if(listCSS == null) {
            System.out.println("Error occurred. Abort!");
            System.exit(-1);
        } else {
            this.contentInfo.getStylesheets().add(listCSS.toURI().toString());
        }

        // Fade Transition
        this.fade.setFromValue(0);
        this.fade.setToValue(1);

        // Blur Effect
        this.shineEffect.setColor(Color.WHITE);
        this.shineEffect.setBlurType(BlurType.GAUSSIAN);
        this.shineEffect.setSpread(0.3);
        this.shineEffect.setHeight(10.0);
        this.shineEffect.setWidth(10.0);
    }

    // Sandwich & Styling:
    public void sandwichLayers() {
        // Mash together:
        this.buttons.getChildren().addAll(add, remove);
        this.imageAndButton.add(buttons, 0, 1);
        this.root.add(contentInfo, 0, 0);             // ORIGINALLY: contentInfo
        this.root.add(imageAndButton, 1, 0);
    }

    /*ADD & REMOVE IMAGES*/
    public void setTargetImage(ImageView inputImage) {
        this.targetImage = inputImage;
        this.targetImage.setEffect(this.shineEffect);
        this.targetImage.setTranslateX(100);
        this.imageAndButton.add(targetImage, 0, 0);
    }

    public void setPotentialTarget(Content inputInfo) {
        this.potentialTarget = inputInfo;
    }

    public void removeTargetImage() {
        this.imageAndButton.getChildren().remove(targetImage);
    }

    /*ADD & REMOVE LIST CONTENT*/
    public void setNewItem(String newItem) {
        this.contentInfo.getItems().add(newItem);
    }

    public void removeListView() {
        this.contentInfo.getItems().clear();
    }

    /*VISIBILITY*/
    public void setVisible() {
        fade.setRate(1);     // Here, the rate plays the animation in the foward direction...
        this.root.setVisible(true);
        fade.play();
    }

    public void setInvisible() {
        fade.setRate(-1);    // As the rate is negative, it is played in reverse...
        this.root.setVisible(false);
        fade.play();
    }

    // Get root node:
    public GridPane getRoot() {
        return this.root;
    }
}
