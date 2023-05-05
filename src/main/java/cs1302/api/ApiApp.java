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
 * ApiApp class.
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
    private Scene scene;
    private VBox root;

    private HBox topRow;
    private Label title;
    private TextField url;
    private Button search;
    private Label instructions;
    private Label movieTitle;
    private Label movieRelease;
    private Label moviePlot;
    private Label movieActors;
    private Label leadActor;
    private Label leadNetWorth;
    private Label leadBirthday;
    private Label leadAge;
    private ImageView thePoster;
    private Label rating;
    private Label disclaimer;
    private Image theImg;

    /**
     * Constructs an {@code ApiApp} object.
     */
    public ApiApp() {
        root = new VBox(5);
        topRow = new HBox();
        title = new Label("SERIES & LEAD ACTOR SEARCH");
        url = new TextField("Type here");
        search = new Button("Search");
        instructions = new Label("Type a movie or television series title into the above"
            + " search bar.");
        movieTitle = new Label("Title: Waiting for info...");
        movieRelease = new Label("Release: Waiting for info...");
        moviePlot = new Label("Plot: Waiting for info...");
        movieActors = new Label("Actors: Waiting for info...");
        leadActor = new Label("Lead Actor: Waiting for info...");
        leadNetWorth = new Label("Lead Actor Net Worth: Waiting for info...");
        leadBirthday = new Label("Lead Actor Birthday: Waiting for info...");
        leadAge = new Label("Lead Actor Age: Waiting for info...");
        thePoster = new ImageView();
        rating = new Label("Movie Rating: Waiting for info...");
        disclaimer = new Label("Disclaimer: If the database does not have certain information"
        + " then that section will display \"N/A\"");
    } // ApiApp

    /**{@inheritDoc} */
    @Override
    public void init() {
        // Initializes the frontend of the app
        root.getChildren().addAll(topRow, instructions,  movieTitle, rating, movieRelease,
            moviePlot, movieActors, thePoster, leadActor, leadNetWorth, leadBirthday, leadAge,
            disclaimer);
        topRow.getChildren().addAll(title, url, search);
        HBox.setHgrow(url, Priority.ALWAYS);
        url.setMaxWidth(Double.MAX_VALUE);
        instructions.setTextAlignment(TextAlignment.CENTER);
        movieTitle.setPrefHeight(25);
        movieRelease.setPrefHeight(25);
        moviePlot.setPrefHeight(75);
        moviePlot.setMaxWidth(500);
        moviePlot.setWrapText(true);
        movieRelease.setPrefHeight(25);
        movieActors.setPrefHeight(25);
        thePoster.setFitHeight(260);
        thePoster.setPreserveRatio(true);
        leadActor.setPrefHeight(25);
        leadNetWorth.setPrefHeight(25);
        leadBirthday.setPrefHeight(25);
        leadAge.setPrefHeight(25);
        rating.setPrefHeight(25);
        disclaimer.setMaxWidth(500);
        disclaimer.setWrapText(true);
        root.setStyle("-fx-background-color: FFEFE0");
        movieTitle.setTextFill(Color.color(1,0,0));
        rating.setTextFill(Color.color(1,0,0));
        movieRelease.setTextFill(Color.color(1,0,0));
        moviePlot.setTextFill(Color.color(1,0,0));
        movieActors.setTextFill(Color.color(1,0,0));
        leadActor.setTextFill(Color.color(0,0,1));
        leadNetWorth.setTextFill(Color.color(0,0,1));
        leadBirthday.setTextFill(Color.color(0,0,1));
        leadAge.setTextFill(Color.color(0,0,1));
    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        scene = new Scene(root);
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.setWidth(500);
        stage.setHeight(700);
        stage.show();
        stage.setResizable(false);

        // The Eventhandler for using the app
        EventHandler<ActionEvent> getMovieInfo = (e) -> {
            getMovie();
        };
        search.setOnAction(getMovieInfo);
    } // start

    /**
     * Requests for and displays the movie data using the inputted name.
     * Also triggers the second API request method afterwards.
     */
    private void getMovie() {
        String movie = URLEncoder.encode(url.getText(), StandardCharsets.UTF_8);
        String apiKey = URLEncoder.encode("84fa6a8f", StandardCharsets.UTF_8);;
        String urlStr =
            String.format("http://www.omdbapi.com/?apikey=%s&t=%s", apiKey, movie);
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
                if (jsonString.equals("{\"Response\":\"False\",\"Error\":\"Movie not found!\"}")) {
                    throw new IllegalArgumentException("This is not a valid movie or series title."
                    + " Please enter a valid title.");
                }
                MovieResult theResult = GSON.fromJson(jsonString, MovieResult.class);
                Platform.runLater(() -> { // Displaying the response information
                    movieTitle.setText("Movie Title: " + theResult.title);
                    movieRelease.setText("Released: " + theResult.released);
                    moviePlot.setText("Plot: " + theResult.plot);
                    movieActors.setText("Actors: " + theResult.actors);
                    theImg = new Image(theResult.poster);
                    thePoster.setImage(theImg);
                    rating.setText("Rating: " + theResult.imdbRating);
                });
                getActorInfo(theResult.actors);
            } catch (IOException | IllegalArgumentException | InterruptedException e) {
                Platform.runLater(() -> alertError(e));
            }
        };
        // Creates a new thread
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    } // getMovie

    /**
     * Requests for and displays the lead actor information.
     * If the database does not have the specificed information then "N/A" is displayed.
     * @param actorList the list of actors from the movie API response.
     */
    private void getActorInfo(String actorList) {
        int commaIndex = actorList.indexOf(",");
        String theActor;
        if (commaIndex > 0) {
            theActor = actorList.substring(0, commaIndex);
        } else {
            theActor = actorList;
        }
        String actor = URLEncoder.encode(theActor, StandardCharsets.UTF_8);
        String urlStr =
            String.format("https://api.api-ninjas.com/v1/celebrity?name=%s"
            , actor);
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
                ActorResult[] resultArray = GSON.fromJson(jsonString, ActorResult[].class);
                if (resultArray.length != 0) {
                    Platform.runLater(() -> { // Displaying the response information
                        leadActor.setText("Lead Actor Name: " + theActor);
                        if (resultArray[0].netWorth == null) {
                            leadNetWorth.setText("Lead Actor Net Worth: N/A");
                        } else {
                            leadNetWorth.setText("Lead Actor Net Worth: " +
                                resultArray[0].netWorth);
                        }
                        if (resultArray[0].birthday == null) {
                            leadBirthday.setText("Lead Actor Birthday: N/A");
                        } else {
                            leadBirthday.setText("Lead Actor Birthday: " + resultArray[0].birthday);
                        }
                        if (resultArray[0].age == null) {
                            leadAge.setText("Lead Actor Age: N/A");
                        } else {
                            leadAge.setText("Lead Actor Age: " + resultArray[0].age);
                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        leadActor.setText("Lead Actor Name: " + theActor);
                        leadNetWorth.setText("Lead Actor Net Worth: N/A");
                        leadBirthday.setText("Lead Actor Birthday: N/A");
                        leadAge.setText("Lead Actor Age: N/A");
                    });
                }
            } catch (IOException | IllegalArgumentException | InterruptedException e) {
                System.err.println(e);
            }
        };
        // Creates a new thread
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    } // getActorInfo

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

}
