package GUI;

import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class FrostPanel {

    Rectangle frostPanel;
    GaussianBlur blurEffect;

    // We need to take an input reference
    InformationPanel infoPanelRef;

    FrostPanel(Stage screen, InformationPanel inputInfoPanel) {
        this.frostPanel = new Rectangle(0,0,0,0);
        this.infoPanelRef = inputInfoPanel;
        this.frostPanel.heightProperty().bind(screen.heightProperty());
        this.frostPanel.widthProperty().bind(screen.widthProperty());
        stylingComponent();
    }

    public void stylingComponent() {
        this.blurEffect = new GaussianBlur(50);
        this.frostPanel.setFill(Color.DARKGREY);
        this.frostPanel.setOpacity(0.30);
        this.frostPanel.setEffect(this.blurEffect);
        this.frostPanel.setVisible(false);
        this.frostPanel.setOnMouseClicked((evt) -> {
            infoPanelRef.removeTargetImage();
            infoPanelRef.removeListView();
            infoPanelRef.setInvisible();
            frostPanel.setVisible(false);
        });
    }

    public void setVisible() {
        this.frostPanel.setVisible(true);
    }

    public void setInvisible() {
        this.frostPanel.setVisible(false);
    }

    public Rectangle getRoot() {
        return this.frostPanel;
    }
}
