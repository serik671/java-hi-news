package javahi.news;

import java.io.FileInputStream;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HelpPane extends Stage{
    private final int width=400,height=180;
    private final String title = "About";
    private final Pane root = new Pane();
    private final Scene scene = new Scene(root,width,height);
    private final Text text = new Text();
    public HelpPane(){
        scene.getStylesheets().add("css/style.css");
        text.getStyleClass().add("text");
        text.setTextAlignment(TextAlignment.LEFT);        
        text.setWrappingWidth(width-10);
        text.setTranslateY(20);
        text.setTranslateX(10);
        readAbout();
        root.getChildren().add(text);
        setScene(scene);
        setTitle(title);
        initModality(Modality.APPLICATION_MODAL);        
        setResizable(false);
        getIcons().add(new Image(getClass().getResourceAsStream("image/HiNews.png")));
        showAndWait();
    }
    private void readAbout(){
        try{
            FileInputStream in = new FileInputStream("About.txt");
            byte[] A = new byte[in.available()];
            in.read(A);
            String str = new String(A);
            text.setText(str);
        }catch(Exception e){            
            text.setText(e.getMessage());
        }
    }
}
