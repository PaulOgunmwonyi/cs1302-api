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
 * On the first scene of the project the user can choose a category and then click generate
 * which well then cause several quotes from that category to be displayed with a button
 * next to each quote.
 * The user chooses whichever quote interests them and clicks the corresponding button.
 * This takes them to a new scene where the quote, its category and the person who said it are
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

    private String[] people = new String[6];
    private String[] quotes = new String[6];

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
        createFirstScene();
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
        stage.setWidth(700);
        stage.setHeight(600);
        stage.show();
        stage.setResizable(false);

        // EventHandler for getting the quotes
        EventHandler<ActionEvent> loadQuotes = (e) -> {
            getQuotes();
            button1.setDisable(false);
            button2.setDisable(false);
            button3.setDisable(false);
            button4.setDisable(false);
            button5.setDisable(false);
            button6.setDisable(false);
        };
        quoteGenerator.setOnAction(loadQuotes);

    } // start


    /**
     * Creates the frontend appearance of {@code root1}, the initial screen the user sees.
     */
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
    }

    private void setAesthetics() {
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

} // ApiApp
