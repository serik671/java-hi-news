package javahi.news;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class JavaHiNews extends Application{

    private static final AnchorPane root = new AnchorPane();
    private static final VBox vbox = new VBox();
    private static final int width=450;
    private static int n = 0, size=0;
    private static  Scene scene = new Scene(root);
    private static final String title = "Java Hi-News";
    private static final String url = "https://hi-news.ru";
    private static Document doc;
    private static Elements elNews;
    private static Image image;
    private static ImageView img = new ImageView();
    private static Text text = new Text();
    private static Text head = new Text();
    private static final Button btnRef = new Button("Читать далее ->");
    private static Button btnR = new Button("_>>");
    private static Button btnL = new Button("<_<");
    private static Text count = new Text();
    private static HBox hbox = new HBox(btnL,count,btnR);
    private static StackPane spane = new StackPane();
    
    @Override public void start(Stage stage)throws Exception{
        scene.getStylesheets().add("css/style.css");  
        img.setFitHeight(256);
        img.setFitWidth(width);
        text.setTextAlignment(TextAlignment.JUSTIFY);
        head.setTextAlignment(TextAlignment.LEFT);
        head.getStyleClass().add("head");
        text.getStyleClass().add("text");
        head.setWrappingWidth(width);
        HBox.setHgrow(btnRef, Priority.ALWAYS);
        vbox.setAlignment(Pos.CENTER);         
        
        Parse();  
        //onError();
        
        scene.setOnKeyPressed(event->{
            if(event.getCode() == KeyCode.RIGHT)RightScroll();
            if(event.getCode() == KeyCode.LEFT)LeftScroll();
        });
        
        btnRef.setOnAction(event->{
            String url;
            url = elNews.get(n).selectFirst("a").attr("href");
            getHostServices().showDocument(url);            
        });        
        
                
        btnL.setMnemonicParsing(true);        
        btnL.setOnAction(event->LeftScroll());
        btnL.setDisable(true);
        
        btnR.setMnemonicParsing(true);
        btnR.setOnAction(event-> RightScroll());        
        hbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER);
        
        text.setWrappingWidth(width-20);
        
        vbox.getChildren().addAll(img,head,text,btnRef); 
        vbox.setSpacing(10);
        //VBox
        AnchorPane.setRightAnchor(vbox, 0.00);
        AnchorPane.setLeftAnchor(vbox, 0.00);
        //Pane
        AnchorPane.setBottomAnchor(spane, 0.00);
        AnchorPane.setRightAnchor(spane, 0.00);
        AnchorPane.setLeftAnchor(spane, 00.00);
        //HBox
        AnchorPane.setRightAnchor(hbox, 100.00);
        AnchorPane.setLeftAnchor(hbox, 10.00);
        
        spane.getChildren().add(hbox);
        spane.getStyleClass().add("pane");
        spane.setAlignment(Pos.CENTER);
        
        root.getChildren().addAll(vbox,spane);
        
        stage.setOnCloseRequest(event->{
            SaveSize(stage);
            System.out.println("Close");            
        });        
        
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("image/HiNews.png")));
        stage.setTitle(title);
        stage.show();
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());  
        double height = stage.getHeight();
        ReadSize(stage);
        stage.setMinHeight(height+50);
    }
    
    private void Parse(){
        try{
            doc = Jsoup.parse(new URL(url), 5000);
            Element main = doc.select("div[id=content]").first();
            elNews = main.select("div[class=roll main-roll]").first().select("article"); 
            String url = elNews.first().selectFirst("div[class=cover]").selectFirst("img").attr("src");
            InputImage(url);
            n = 0;
            head.setText(elNews.first().selectFirst("a").text());
            text.setText(elNews.first().selectFirst("p").text());
            size = elNews.size();
            count.setText(String.format("(%d/%d)",n+1,size));
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
    private void Load(int a){
        head.setText(elNews.get(a).selectFirst("a").text());
        text.setText(elNews.get(a).selectFirst("p").text());
        String url;
        if(a==0)url = elNews.get(a).selectFirst("img").attr("src");
        else url= elNews.get(a).selectFirst("img").attr("data-src");
        InputImage(url);
        count.setText(String.format("(%d/%d)",n+1,size));
        
    }
    private void InputImage(String url){
        try{
            URLConnection connection = new URL(url).openConnection();
            connection.addRequestProperty("User-Agent", "Mypal");          
            InputStream input = connection.getInputStream();
            image = new Image(input);
            img.setImage(image);
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    
    private void RightScroll(){
        if(n>=size-1)return;
        n++;
        if(n==size-1)btnR.setDisable(true);
        else if(n==1)btnL.setDisable(false);
        Load(n);
    }
    private void LeftScroll(){
        if(n<=0)return;
        n--;
        if(n==0)btnL.setDisable(true);
        else if(n==size-2)btnR.setDisable(false);
        Load(n);
    }
    
    private void SaveSize(Stage stage){
        try{
            File file = new File("size.jhn");            
            String size;            
            size = stage.isMaximized() ? "max" :String.format("%f;%f;%f;%f",stage.getWidth(),stage.getHeight(),stage.getX(),stage.getY());
            FileOutputStream out = new FileOutputStream(file);
            out.write(size.getBytes());
            out.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void ReadSize(Stage stage){
        try{
            File file = new File("size.jhn");
            FileInputStream in = new FileInputStream(file);
            byte [] B = new byte [in.available()];
            in.read(B);
            String size = new String(B);
            size = size.replaceAll(",",".");
            if(size.equals("max")){
                stage.setMaximized(true);
                return;
            };
            stage.setWidth(Double.valueOf(size.split(";")[0]));
            stage.setHeight(Double.valueOf(size.split(";")[1]));
            stage.setX(Double.valueOf(size.split(";")[2]));
            stage.setY(Double.valueOf(size.split(";")[3]));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args){
        Application.launch(args);
    }
}