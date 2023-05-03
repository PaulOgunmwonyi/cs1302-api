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
 * On the first screen of the project the user can choose a category and then click generate
 * which well then cause several quotes from that category to be displayed with a button
 * next to each quote.
 * The user chooses whichever quote interests them and clicks the corresponding button.
 * This takes them to a new scene root where the quote, its category and the person who said it are
 * are displayed.
 * Below this are several buttons with languages the user can turn the quote into.
 * After they click the language the quote is displayed below.
 * The user can use a button at the top of this scene to go back to the original scene.
 */
public class ApiApp extends Application {
    /** The HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    /** Google {@code Gson} object for parsing JSOn-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    private Stage stage;
    private Scene scene1;
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

    private HBox scene2TopRow;
    private Label instructions2;
    private Label author;
    private Label category;
    private Label originalQuote;
    private HBox buttonRow;
    private Label translatedQuote;

    private Label title2;
    private Button backButton;
    private Button englishButton;
    private Button spanishButton;
    private Button frenchButton;
    private Button germanButton;

    private String[] people = new String[6];
    private String[] quotes = new String[6];

    EventHandler<ActionEvent> loadQuotes;
    EventHandler<ActionEvent> loadSecondScene1;
    EventHandler<ActionEvent> loadSecondScene2;
    EventHandler<ActionEvent> loadSecondScene3;
    EventHandler<ActionEvent> loadSecondScene4;
    EventHandler<ActionEvent> loadSecondScene5;
    EventHandler<ActionEvent> loadSecondScene6;
    EventHandler<ActionEvent> goBack;

    private static final String US_FLAG = "resources/Flag_of_the_United_States.png";
    private static final String SP_FLAG = "resources/Spain_flag.png";
    private static final String FR_FLAG = "resources/Flag_of_France.png";
    private static final String DE_FLAG = "resources/Germany_flag.png";

    Image usFlag = new Image("file:" + US_FLAG);
    Image spFlag = new Image("file:" + SP_FLAG);
    Image frFlag = new Image("file:" + FR_FLAG);
    Image deFlag = new Image("file:" + DE_FLAG);

    ImageView usImgView = new ImageView(usFlag);
    ImageView spImgView = new ImageView(spFlag);
    ImageView frImgView = new ImageView(frFlag);
    ImageView deImgView = new ImageView(deFlag);

    /**
     * Constructs an {@code ApiApp} object.
     */
    public ApiApp() {
        // constructs the first root
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
        // constructs the second root
        root2 = new VBox(5);
        scene2TopRow = new HBox(5);
        title2 = new Label("RANDOM QUOTE GENERATOR & TRANSLATOR");
        backButton = new Button("Go back to previous");
        instructions2 = new Label("Below is the chosen quote's info. Choose a language flag to" +
            " translate your quote into that language. Once you are satisfied you can click the" +
            " go back button at the top.");
        author = new Label();
        category = new Label();
        originalQuote = new Label();
        buttonRow = new HBox();
        englishButton = new Button();
        spanishButton = new Button();
        frenchButton = new Button();
        germanButton = new Button();
        translatedQuote = new Label("Translated quote will appear here");
    } // ApiApp

    /**{@inheritDoc} */
    @Override
    public void init() {
        // creates the appearance of the initial scene
        createFirstScene();
        setAesthetics();
        // creates the appearance of the second scene
        createSecondScene();
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
        stage.setWidth(700);
        stage.setHeight(600);
        stage.show();
        stage.setResizable(false);

        // creates and sets all of the handlers to their appropriate buttons
        createHandlers();
        quoteGenerator.setOnAction(loadQuotes);
        button1.setOnAction(loadSecondScene1);
        button2.setOnAction(loadSecondScene2);
        button3.setOnAction(loadSecondScene3);
        button4.setOnAction(loadSecondScene4);
        button5.setOnAction(loadSecondScene5);
        button6.setOnAction(loadSecondScene6);
        backButton.setOnAction(goBack);

    } // start

    private void createFirstScene() {
        root1.setStyle("-fx-background-color: FFEFE0");
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
        quoteBar1.setPrefHeight(77);
        quoteBar2.setPrefHeight(77);
        quoteBar3.setPrefHeight(77);
        quoteBar4.setPrefHeight(77);
        quoteBar5.setPrefHeight(77);
        quoteBar6.setPrefHeight(77);
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
        quote1.setMaxWidth(560);
        quote1.setWrapText(true);
        quote2.setMaxWidth(560);
        quote2.setWrapText(true);
        quote3.setMaxWidth(560);
        quote3.setWrapText(true);
        quote4.setMaxWidth(560);
        quote4.setWrapText(true);
        quote5.setMaxWidth(560);
        quote5.setWrapText(true);
        quote6.setMaxWidth(560);
        quote6.setWrapText(true);
    } // createFirstScene

    private void createSecondScene() {
        root2.setStyle("-fx-background-color: FFEFE0");
        root2.getChildren().addAll(scene2TopRow, instructions2, author, category, originalQuote,
            buttonRow, translatedQuote);
        scene2TopRow.getChildren().addAll(title2, backButton);
        buttonRow.getChildren().addAll(englishButton, spanishButton, frenchButton, germanButton);
        title2.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(title2, Priority.ALWAYS);
        instructions2.setTextAlignment(TextAlignment.CENTER);
        instructions2.setMaxWidth(600);
        instructions2.setWrapText(true);
        originalQuote.setMaxWidth(600);
        originalQuote.setWrapText(true);
        originalQuote.setPrefHeight(80);
        usImgView.setFitWidth(177);
        usImgView.setPreserveRatio(true);
        spImgView.setFitWidth(142);
        spImgView.setPreserveRatio(true);
        frImgView.setFitWidth(142);
        frImgView.setPreserveRatio(true);
        deImgView.setFitWidth(162);
        deImgView.setPreserveRatio(true);
        englishButton.setGraphic(usImgView);
        spanishButton.setGraphic(spImgView);
        frenchButton.setGraphic(frImgView);
        germanButton.setGraphic(deImgView);
        englishButton.setMaxWidth(Double.MAX_VALUE);
        spanishButton.setMaxWidth(Double.MAX_VALUE);
        frenchButton.setMaxWidth(Double.MAX_VALUE);
        germanButton.setMaxWidth(Double.MAX_VALUE);
        translatedQuote.setMaxWidth(600);
        translatedQuote.setWrapText(true);
        translatedQuote.setPrefHeight(80);
    } // createSecondScene

    private void setAesthetics() {
        quote1.setTextFill(Color.color(1,0,0));
        button1.setStyle("-fx-background-color: #ff0000");
        quote2.setTextFill(Color.color(1,.5,0));
        button2.setStyle("-fx-background-color: #ff8000");
        quote3.setTextFill(Color.color(0,1,0));
        button3.setStyle("-fx-background-color: #00ff00");
        quote4.setTextFill(Color.color(0,1,1));
        button4.setStyle("-fx-background-color: #00ffff");
        quote5.setTextFill(Color.color(0,0,1));
        button5.setStyle("-fx-background-color: #0000ff");
        quote6.setTextFill(Color.color(1,0,1));
        button6.setStyle("-fx-background-color: #ff00ff");
    } // setAesthetics

    private void createHandlers() {
        // EventHandler for getting the quotes
        loadQuotes = (e) -> {
            getQuotes();
            button1.setDisable(false);
            button2.setDisable(false);
            button3.setDisable(false);
            button4.setDisable(false);
            button5.setDisable(false);
            button6.setDisable(false);
        };
        // EventHandlers for loading the second screen
        loadSecondScene1  = (e) -> {
            updateQuoteDisplay(0);
            scene1.setRoot(root2);
        };
        loadSecondScene2  = (e) -> {
            updateQuoteDisplay(1);
            scene1.setRoot(root2);
        };
        loadSecondScene3  = (e) -> {
            updateQuoteDisplay(2);
            scene1.setRoot(root2);
        };
        loadSecondScene4  = (e) -> {
            updateQuoteDisplay(3);
            scene1.setRoot(root2);
        };
        loadSecondScene5  = (e) -> {
            updateQuoteDisplay(4);
            scene1.setRoot(root2);
        };
        loadSecondScene6  = (e) -> {
            updateQuoteDisplay(5);
            scene1.setRoot(root2);
        };
        // EventHandler for going back to the first scene
        goBack = ((e) -> scene1.setRoot(root1));

    } // createHandlers

    private void getQuotes() {
        // Creates the url to be sent
        String category = URLEncoder.encode(dropDown.getValue(), StandardCharsets.UTF_8);
        String limit = URLEncoder.encode("6", StandardCharsets.UTF_8);
        String urlStr =
            String.format("https://api.api-ninjas.com/v1/quotes?category=%s&limit=%s",
            category, limit);
        // The main Runnable
        Runnable task = () -> {
            try {
                // Creates and sends the request then recieves the response
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlStr))
                    .header("X-Api-Key" ,"znBjJAWWz/khdtaf2dPDrw==DzYrOkIE6Uu7heiF")
                    .build();
                HttpResponse<String> response = HTTP_CLIENT
                    .send(request, BodyHandlers.ofString());
                String jsonString = response.body().toString();
                QuoteResult[] quoteArray = GSON.fromJson(jsonString, QuoteResult[].class);
                int count = 0;
                for (QuoteResult theQuote : quoteArray) {
                    quotes[count] = theQuote.quote;
                    people[count] = theQuote.author;
                    count++;
                }
                Platform.runLater(() -> quote1.setText(quotes[0]));
                Platform.runLater(() -> quote2.setText(quotes[1]));
                Platform.runLater(() -> quote3.setText(quotes[2]));
                Platform.runLater(() -> quote4.setText(quotes[3]));
                Platform.runLater(() -> quote5.setText(quotes[4]));
                Platform.runLater(() -> quote6.setText(quotes[5]));
            } catch (IOException | IllegalArgumentException | InterruptedException e) {
                System.err.println(e);
            }
        };
        // Creates a new thread
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    } // getQuotes

    private void updateQuoteDisplay(int num) {
        author.setText("Author of quote: " + people[num]);
        category.setText("Category of quote: " + dropDown.getValue());
        originalQuote.setText("Original quote: " + quotes[num]);
    } // updateQuoteDisplay

} // ApiApp
