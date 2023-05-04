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
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This project is a random Stranger Things quote generator that can later be used to see info
 * about the character who said the chosen quote.
 * On the first screen of the project the user can click generate which will then cause several
 * quotes from that category to be displayed with a button next to each quote.
 * The user chooses whichever quote interests them and clicks the corresponding button.
 * This takes them to a new scene root where the quote, the character who said it and info about
 * the character are displayed.
 * The user can use a button at the top of this scene to go back to the original root.
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
    private Label theQuote;
    private Label title2;
    private Button backButton;
    private ImageView charImgView;
    private Image charImg;
    private Label actor;
    private Label theAliases;
    private Label dateBorn;

    // Storage Arrays
    private String[] people = new String[6];
    private String[] quotes = new String[6];

    // EventHandlers
    EventHandler<ActionEvent> loadQuotes;
    EventHandler<ActionEvent> loadSecondScene1;
    EventHandler<ActionEvent> loadSecondScene2;
    EventHandler<ActionEvent> loadSecondScene3;
    EventHandler<ActionEvent> loadSecondScene4;
    EventHandler<ActionEvent> loadSecondScene5;
    EventHandler<ActionEvent> loadSecondScene6;
    EventHandler<ActionEvent> goBack;
    EventHandler<ActionEvent> getInfo;

    /**
     * Constructs an {@code ApiApp} object.
     */
    public ApiApp() {
        // constructs the first root
        root1 = new VBox(5);
        scene1TopRow = new HBox(5);
        instructions1 = new Label("Click generate to generate random Stranger Things quotes. Once" +
        " you get a quote you like click the button next to it to choose it.");
        quoteBar1 = new HBox();
        quoteBar2 = new HBox();
        quoteBar3 = new HBox();
        quoteBar4 = new HBox();
        quoteBar5 = new HBox();
        quoteBar6 = new HBox();
        title = new Label("STRANGER THINGS QUOTE & CHARACTER INFO GENERATOR");
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
        title2 = new Label("STRANGER THINGS QUOTE & CHARACTER INFO GENERATOR");
        backButton = new Button("Go back to previous");
        instructions2 = new Label("Below is the chosen quote and info about the character" +
            " that said it. Once you are satisfied you can click the go back button at the top.");
        author = new Label();
        theQuote = new Label();
        charImgView = new ImageView();
        actor = new Label();
        theAliases = new Label();
        dateBorn = new Label();
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
        // sets up the stage
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

    /**
     * Creates the frontend appearance of {@code root1}, the initial screen the user sees.
     */
    private void createFirstScene() {
        root1.setStyle("-fx-background-color: FFEFE0");
        root1.getChildren().addAll(scene1TopRow, instructions1, quoteBar1, quoteBar2, quoteBar3,
            quoteBar4, quoteBar5, quoteBar6);
        scene1TopRow.getChildren().addAll(title, quoteGenerator);
        quoteBar1.getChildren().addAll(quote1, button1);
        quoteBar2.getChildren().addAll(quote2, button2);
        quoteBar3.getChildren().addAll(quote3, button3);
        quoteBar4.getChildren().addAll(quote4, button4);
        quoteBar5.getChildren().addAll(quote5, button5);
        quoteBar6.getChildren().addAll(quote6, button6);
        title.setMaxWidth(Double.MAX_VALUE);
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

    /**
     * Creates the frontend appearance of {@code root2}, the secondary screen the user can access.
     */
    private void createSecondScene() {
        root2.setStyle("-fx-background-color: FFEFE0");
        root2.getChildren().addAll(scene2TopRow, instructions2, theQuote, author, charImgView,
            actor, theAliases, dateBorn);
        scene2TopRow.getChildren().addAll(title2, backButton);
        title2.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(title2, Priority.ALWAYS);
        instructions2.setTextAlignment(TextAlignment.CENTER);
        instructions2.setMaxWidth(600);
        instructions2.setWrapText(true);
        theQuote.setMaxWidth(600);
        theQuote.setWrapText(true);
        theQuote.setPrefHeight(80);
        charImgView.setFitHeight(325);
        charImgView.setPreserveRatio(true);
        theAliases.setMaxWidth(600);
        theAliases.setWrapText(true);
    } // createSecondScene

    /**
     * Adds color to the Buttons and Labels of the body of {@code root1}.
     */
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

    /**
     * Assigns the EventHandlers to their appropriate actions.
     */
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
            getQuoteInfo(0);
        };
        loadSecondScene2  = (e) -> {
            updateQuoteDisplay(1);
            getQuoteInfo(1);
        };
        loadSecondScene3  = (e) -> {
            updateQuoteDisplay(2);
            getQuoteInfo(2);
        };
        loadSecondScene4  = (e) -> {
            updateQuoteDisplay(3);
            getQuoteInfo(3);
        };
        loadSecondScene5  = (e) -> {
            updateQuoteDisplay(4);
            getQuoteInfo(4);
        };
        loadSecondScene6  = (e) -> {
            updateQuoteDisplay(5);
            getQuoteInfo(5);
        };
        // EventHandler for going back to the first scene
        goBack = ((e) -> scene1.setRoot(root1));
    } // createHandlers

    /**
     * Requests 6 quotes from the Random Stranger Things Quote API and then stores and
     * displays the received quotes.
     */
    private void getQuotes() {
        String limit = URLEncoder.encode("6", StandardCharsets.UTF_8);
        String urlStr =
            String.format("https://strangerthings-quotes.vercel.app/api/quotes/%s"
            , limit);
        // The main Runnable
        Runnable task = () -> {
            try {
                // Creates and sends the request then recieves the response
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlStr))
                    .build();
                HttpResponse<String> response = HTTP_CLIENT
                    .send(request, BodyHandlers.ofString());
                String jsonString = response.body().toString();
                // Converts the response then stores and displays
                QuoteResult[] resultArray = GSON.fromJson(jsonString, QuoteResult[].class);
                int count = 0;
                for (QuoteResult theResult : resultArray) {
                    quotes[count] = theResult.quote;
                    people[count] = theResult.author;
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

    /**
     * Updates the character and quote Labels with the current quote and its character.
     * @param num is the quote number from 0-5.
     */
    private void updateQuoteDisplay(int num) {
        author.setText("Character: " + people[num]);
        theQuote.setText("Quote: \"" + quotes[num] + "\"");
    } // updateQuoteDisplay

    /**
     * Requests the info for the current character from the Stranger Things Character API.
     * Displays a photo of the character, the character's birth year, the actor who portrayed
     * them and any in series aliases.
     * @param num is the quote number from 0-5.
     */
    private void getQuoteInfo(int num) {
        String charName = URLEncoder.encode(people[num], StandardCharsets.UTF_8);
        String urlStr =
            String.format("https://stranger-things-api.fly.dev/api/v1/characters?name=%s"
            , charName);
        // The main Runnable
        Runnable task = () -> {
            try {
                // Creates and sends the request then recieves the response
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlStr))
                    .build();
                HttpResponse<String> response = HTTP_CLIENT
                    .send(request, BodyHandlers.ofString());
                String jsonString = response.body().toString();
                InfoResult[] theResult = GSON.fromJson(jsonString, InfoResult[].class);
                if (theResult.length == 0) {
                    throw new IllegalArgumentException("There is no character info for this"
                    + " character please pick another quote.");
                }
                if (people[num].equals("Max Mayfield")) {
                    throw new IllegalArgumentException("There is insufficient character" +
                    " info for this character please pick another quote.");
                }
                charImg = new Image(theResult[0].photo);
                Platform.runLater(() -> charImgView.setImage(charImg));
                Platform.runLater(() -> actor.setText("Portrayed By: " + theResult[0].portrayedBy));
                String allAliases = "Aliases: ";
                for (String current : theResult[0].aliases) {
                    allAliases += (current + ", ");
                }
                final String a = allAliases.substring(0, allAliases.length() - 2);
                Platform.runLater(() -> theAliases.setText(a));
                Platform.runLater(() -> dateBorn.setText("Birth Year: " + theResult[0].born));
                Platform.runLater(() -> scene1.setRoot(root2));
            } catch (IOException | IllegalArgumentException | InterruptedException e) {
                Platform.runLater(() -> alertError(e));
            }
        };
        // Creates a new thread
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    } // getQuoteInfo

    /**
     * Show a error alert based on {@code cause}.
     * @param cause a {@link java.lang.Throwable Throwable} that cause the alert
     */
    private void alertError(Throwable cause) {
        TextArea text = new TextArea("Exception: " + cause.toString());
        text.setEditable(false);
        Alert alert = new Alert(AlertType.ERROR);
        alert.getDialogPane().setContent(text);
        alert.setResizable(false);
        alert.showAndWait();
    } // alertError

} // ApiApp
