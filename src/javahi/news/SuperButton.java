package javahi.news;

import javafx.animation.Animation;
import javafx.scene.shape.Rectangle;
import javafx.animation.FillTransition;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SuperButton extends StackPane{
    private Rectangle recF = new Rectangle();
    private Text textT = new Text();
    private FillTransition fillT = new FillTransition(Duration.millis(1000),recF);
    private String text;
    private double width,height;
    public SuperButton(String text, double width, double height){
        this.text = text;
        this.width = width;
        this.height = height;
        textT.getStyleClass().add("text");
        recF.getStyleClass().add("super");
        textT.setText(text);
        textT.setFont(Font.font(14));
        textT.setDisable(true);
        recF.setWidth(width);
        recF.setHeight(height);
        fillT.setFromValue(Color.GREENYELLOW);
        fillT.setToValue(Color.ORANGE);
        fillT.setAutoReverse(true);
        fillT.setCycleCount(Animation.INDEFINITE);
        getChildren().addAll(recF,textT);
        setOnMouseEntered(event->{
            fillT.play();            
        });
        setOnMouseExited(event->{
            fillT.stop();
        });
    }
}
