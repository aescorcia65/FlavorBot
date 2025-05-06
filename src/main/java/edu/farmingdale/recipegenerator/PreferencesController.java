package edu.farmingdale.recipegenerator;

import com.mysql.cj.xdevapi.JsonArray;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

public class PreferencesController {


    @FXML
    Button updateButton;
    @FXML
    private ComboBox<String> foodStyleComboBox;
    @FXML
    private ComboBox<String> dietaryPreferencesComboBox;
    @FXML
    private ComboBox<String> mealTypeComboBox;
    @FXML
    private TextArea allergiesTextArea,notesTextArea;

    @FXML
    private ComboBox<String> skillsComboBox,portionSizeComboBox,cookingTimeComboBox,flavorComboBox,messComboBox;
    @FXML
    private Label dietaryPreferencesLabel,mealTypeLabel,spiceLevelLabel,skillsLabel,foodStyleLabel,allergiesLabel,portionSizeLabel,numServingLabel,cokingTimeLabel,flavorLabel,messLabel,notesLabel;
    @FXML
    private ComboBox<Integer> spiceLevelSlider,numServingComboBox;

    @FXML
    private TextField ingredientsAvailableField;

    @FXML
    private TableView<Ingredient> ingredientsTable;

    @FXML
    private HBox hBoxholder;

    @FXML
    private AnchorPane anchorPane; // The AnchorPane from the FXML
    @FXML
    ImageView backgroundImageView;

    @FXML
    private GridPane ingredientsGrid;

    @FXML
    private TableColumn<Ingredient, String> ingredientNameColumn;
    @FXML
    private TableColumn<Ingredient, String> ingredientQuantityColumn;
    @FXML
    private TableColumn<Ingredient, String> ingredientCategoryColumn;

    private final ObservableList<Ingredient> ingredientData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1) Parse the prefs JSON
        String json = SessionManager.getInstance()
                .getCurrentUser()
                .getPreferencesJson();
        JSONObject prefs = new JSONObject(json);

        // 2) Populate your controls
        spiceLevelSlider.getItems().addAll(1,2,3,4,5,6,7,8,9,10);
        foodStyleComboBox.getItems().addAll("None","Italian","Chinese","Mexican","Indian","American","Japanese");
        dietaryPreferencesComboBox.getItems().addAll("None","Vegetarian","Vegan","Gluten-Free","Dairy-Free");
        mealTypeComboBox.getItems().addAll("None","Breakfast","Lunch","Dinner","Snack");
        skillsComboBox.getItems().addAll("None","Beginner","Intermediate","Advanced");
        portionSizeComboBox.getItems().addAll("None","Small","Medium","Large");
        cookingTimeComboBox.getItems().addAll("None","15min","30min","1hour","2hours");
        flavorComboBox.getItems().addAll("None","sweet","salty","sour","bitter","umami");
        messComboBox.getItems().addAll("Minimal","Medium","Doesn’t matter");
        numServingComboBox.getItems().addAll(1,2,3,4,5,6,7,8,9,10);

        // 3) Read out the saved values (with defaults)
        String sf = prefs.optString("foodStyle",        "None");
        String sd = prefs.optString("dietaryPreference","None");
        String sm = prefs.optString("mealType",         "None");
        int    sp = prefs.optInt   ("spiceLevel",        0);
        String sk = prefs.optString("cookingSkill",     "None");
        String ps = prefs.optString("portionSize",      "None");
        String ct = prefs.optString("cookingTime",      "None");
        String fp = prefs.optString("flavorProfile",    "None");
        String ce = prefs.optString("cleanupEffort",    "None");
        int    ns = prefs.optInt   ("numberOfServings",  1);
        String al = prefs.optString("allergies",        "");
        String ad = prefs.optString("additionalNotes",  "");

        // 4) Apply them to the UI
        foodStyleComboBox.getSelectionModel().select(sf);
        dietaryPreferencesComboBox.getSelectionModel().select(sd);
        mealTypeComboBox.getSelectionModel().select(sm);
        spiceLevelSlider.setValue(sp);
        skillsComboBox.getSelectionModel().select(sk);
        portionSizeComboBox.getSelectionModel().select(ps);
        cookingTimeComboBox.getSelectionModel().select(ct);
        flavorComboBox.getSelectionModel().select(fp);
        messComboBox.getSelectionModel().select(ce);
        numServingComboBox.getSelectionModel().select(Integer.valueOf(ns));
        allergiesTextArea.setText(al);
        notesTextArea.setText(ad);

        // 5) (Optional) load & bind a background image
        Image img = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/images/preferences.png")));
        backgroundImageView.setImage(img);
        //backgroundImageView.setPreserveRatio(true);
        backgroundImageView.setFitWidth(anchorPane.getWidth());
        backgroundImageView.setFitHeight(anchorPane.getHeight());
        anchorPane.widthProperty().addListener((o,oldN,newN) ->
                backgroundImageView.setFitWidth(newN.doubleValue()));
        anchorPane.heightProperty().addListener((o,oldN,newN) ->
                backgroundImageView.setFitHeight(newN.doubleValue()));
    }

    @FXML
    private void handleContinueButtonAction() {
        // Retrieve user selections, defaulting to empty string or safe value if null
        String selectedFoodStyle = foodStyleComboBox.getValue() != null ? foodStyleComboBox.getValue() : "";
        String selectedDietaryPreference = dietaryPreferencesComboBox.getValue() != null ? dietaryPreferencesComboBox.getValue() : "";
        String selectedMealType = mealTypeComboBox.getValue() != null ? mealTypeComboBox.getValue() : "";
        int spiceLevel = spiceLevelSlider.getValue() != null ? spiceLevelSlider.getValue() : 0;
        String selectedSkills = skillsComboBox.getValue() != null ? skillsComboBox.getValue() : "";
        String portionSelected = portionSizeComboBox.getValue() != null ? portionSizeComboBox.getValue() : "";
        String cookingTimeSelected = cookingTimeComboBox.getValue() != null ? cookingTimeComboBox.getValue() : "";
        String flavorSelected = flavorComboBox.getValue() != null ? flavorComboBox.getValue() : "";
        String messSelected = messComboBox.getValue() != null ? messComboBox.getValue() : "";
        int numberOfServingsSelected = numServingComboBox.getValue() != null ? numServingComboBox.getValue() : 1;
        String allergiesSelected = allergiesTextArea.getText() != null ? allergiesTextArea.getText() : "";
        String notesSelected = notesTextArea.getText() != null ? notesTextArea.getText() : "";

         //Validate selections
        if (selectedFoodStyle == null || selectedDietaryPreference == null || selectedMealType == null) {
            showAlert("Missing Selection", "Please select food style, dietary preference, and meal type.");
            return;
        }

        // Build full JSON with all preferences
        String json = String.format(
                "{" +
                        "\"foodStyle\":\"%s\"," +
                        "\"dietaryPreference\":\"%s\"," +
                        "\"mealType\":\"%s\"," +
                        "\"spiceLevel\":%d," +
                        "\"cookingSkill\":\"%s\"," +
                        "\"portionSize\":\"%s\"," +
                        "\"cookingTime\":\"%s\"," +
                        "\"flavorProfile\":\"%s\"," +
                        "\"cleanupEffort\":\"%s\"," +
                        "\"numberOfServings\":%d," +
                        "\"allergies\":\"%s\"," +
                        "\"additionalNotes\":\"%s\"" +
                        "}",
                selectedFoodStyle, selectedDietaryPreference, selectedMealType, spiceLevel,
                selectedSkills, portionSelected, cookingTimeSelected, flavorSelected,
                messSelected, numberOfServingsSelected, allergiesSelected, notesSelected
        );

        // Update preferences in database
        AzureDBConnector connector = new AzureDBConnector();
        connector.updateUserPreferences(
                SessionManager.getInstance().getCurrentUser().getUserID(), json
        );

        // Open next window
        openMainWindow();
    }
    private void openMainWindow() {
        try {
            // Close the login window

            // Load the main scene (your fridge management window)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/farmingdale/recipegenerator/hello-view.fxml"));
            Parent root = loader.load();

            // Get the screen bounds
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double screenWidth = screenBounds.getWidth();
            double screenHeight = screenBounds.getHeight();

            // Add the external CSS stylesheet
            Scene scene = new Scene(root, screenWidth * 1, screenHeight * 0.98);
            scene.getStylesheets().add(getClass().getResource("/Styling/frosted-glass.css").toExternalForm());

            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Flavor Bot");
            newStage.show();
            ((Stage) mealTypeComboBox.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load the main window.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner Ingredient class
    public static class Ingredient {
        private final StringProperty ingredient;

        public Ingredient(String ingredient) {
            this.ingredient = new SimpleStringProperty(ingredient);
        }

        public String getIngredient() {
            return ingredient.get();
        }

        public void setIngredient(String ingredient) {
            this.ingredient.set(ingredient);
        }

        public StringProperty ingredientProperty() {
            return ingredient;
        }
    }

}
