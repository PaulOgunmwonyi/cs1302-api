package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URI;
import java.net.URLEncoder;
import javafx.scene.Node;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Labeled;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import javafx.scene.control.ComboBox;

/**
 * This project is a random quote generator that can be later used to translate that quote
 * into one of the displayed languages.
 * On the first scene of the project the user can choose a category and then several quotes
 * from that category are displayed with a button next to each quote.
 * The user chooses whichever quote interests them and clicks the corresponding button.
 * This takes them to a new scene where the quote, its category and the person who said it are
 * are displayed.
 * Below this are several buttons with languages the user can turn the quote into.
 * After they click the language the quote is displayed below.
 * The user can use a button at the top of this scene to go back to the original scene.
 */
public class ApiApp extends Application {
    private Stage stage;
    private Scene scene1;
    private Scene scene2;
    private VBox root1;
    private VBox root2;

    private HBox scene1TopRow;
    private Label instructions1;
    private HBox quoteBar1;
    private HBox quoteBar2;
    private HBox quoteBar3;
    private HBox quoteBar4;
    private HBox quoteBar5;
    private HBox quoteBar6;

    private Label title;
    private ComboBox<String> dropDown;
    private Button quoteGenerator;
    private Label quote1;
    private Label quote2;
    private Label quote3;
    private Label quote4;
    private Label quote5;
    private Label quote6;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;

    /**
     * Constructs an {@code ApiApp} object.
     */
    public ApiApp() {
        root1 = new VBox(5);
        scene1TopRow = new HBox(5);
        instructions1 = new Label("Select your preferred category from the drop down and then" +
            " click generate to generate random quotes. Once you get a quote you like click the" +
            " button next to it to choose it");
        quoteBar1 = new HBox();
        quoteBar2 = new HBox();
        quoteBar3 = new HBox();
        quoteBar4 = new HBox();
        quoteBar5 = new HBox();
        quoteBar6 = new HBox();
        title = new Label("RANDOM QUOTE GENERATOR & TRANSLATOR");
        dropDown = new ComboBox<String>();
        quoteGenerator = new Button("Generate");
        quote1 = new Label("Waiting for quotes...");
        quote2 = new Label("Waiting for quotes...");
        quote3 = new Label("Waiting for quotes...");
        quote4 = new Label("Waiting for quotes...");
        quote5 = new Label("Waiting for quotes...");
        quote6 = new Label("Waiting for quotes...");
        button1 = new Button("Choose this quote");
        button2 = new Button("Choose this quote");
        button3 = new Button("Choose this quote");
        button4 = new Button("Choose this quote");
        button5 = new Button("Choose this quote");
        button6 = new Button("Choose this quote");
    } // ApiApp

    /**{@inheritDoc} */
    @Override
    public void init() {
        // creates the appearance of the initial scene
        root1.getChildren().addAll(scene1TopRow, instructions1, quoteBar1, quoteBar2, quoteBar3,
            quoteBar4, quoteBar5, quoteBar6);
        scene1TopRow.getChildren().addAll(title, dropDown, quoteGenerator);
        quoteBar1.getChildren().addAll(quote1, button1);
        quoteBar2.getChildren().addAll(quote2, button2);
        quoteBar3.getChildren().addAll(quote3, button3);
        quoteBar4.getChildren().addAll(quote4, button4);
        quoteBar5.getChildren().addAll(quote5, button5);
        quoteBar6.getChildren().addAll(quote6, button6);
        title.setMaxWidth(Double.MAX_VALUE);
        dropDown.getItems().addAll("age", "anger", "beauty", "courage", "fitness", "funny",
            "happiness", "humor", "inspirational", "love", "money");
        dropDown.getSelectionModel().select(0);
        instructions1.setTextAlignment(TextAlignment.CENTER);
        instructions1.setMaxWidth(600);
        instructions1.setWrapText(true);
        quoteBar1.setPrefHeight(60);
        quoteBar2.setPrefHeight(60);
        quoteBar3.setPrefHeight(60);
        quoteBar4.setPrefHeight(60);
        quoteBar5.setPrefHeight(60);
        quoteBar6.setPrefHeight(60);
        button1.setDisable(true);
        button2.setDisable(true);
        button3.setDisable(true);
        button4.setDisable(true);
        button5.setDisable(true);
        button6.setDisable(true);
        HBox.setHgrow(title, Priority.ALWAYS);
        HBox.setHgrow(quote1, Priority.ALWAYS);
        HBox.setHgrow(quote2, Priority.ALWAYS);
        HBox.setHgrow(quote3, Priority.ALWAYS);
        HBox.setHgrow(quote4, Priority.ALWAYS);
        HBox.setHgrow(quote5, Priority.ALWAYS);
        HBox.setHgrow(quote6, Priority.ALWAYS);
        quote1.setMaxWidth(460);
        quote1.setWrapText(true);
        quote2.setMaxWidth(460);
        quote2.setWrapText(true);
        quote3.setMaxWidth(460);
        quote3.setWrapText(true);
        quote4.setMaxWidth(460);
        quote4.setWrapText(true);
        quote5.setMaxWidth(460);
        quote5.setWrapText(true);
        quote6.setMaxWidth(460);
        quote6.setWrapText(true);
        setAesthetics();
    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        // setup stage
        this.stage = stage;
        scene1 = new Scene(root1);
        stage.setTitle("ApiApp!");
        stage.setScene(scene1);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.setWidth(600);
        stage.setHeight(500);
        stage.show();
        stage.setResizable(false);
    } // start


    public void setAesthetics() {
        quote1.setTextFill(Color.color(1,0,0));
        button1.setStyle("-fx-background-color: #ff0000");
        quote2.setTextFill(Color.color(1,.5,0));
        button2.setStyle("-fx-background-color: #fff000");
        quote3.setTextFill(Color.color(0,1,0));
        button3.setStyle("-fx-background-color: #00ff00");
        quote4.setTextFill(Color.color(0,1,1));
        button4.setStyle("-fx-background-color: #00ffff");
        quote5.setTextFill(Color.color(0,0,1));
        button5.setStyle("-fx-background-color: #0000ff");
        quote6.setTextFill(Color.color(1,0,1));
        button6.setStyle("-fx-background-color: #ff00ff");
    }
} // ApiApp
