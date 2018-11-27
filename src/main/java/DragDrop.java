import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class DragDrop extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 551, 400);
        ListView<String> list = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();

        scene.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                event.consume();
            }
        });

        // Dropping over surface
        scene.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                String filePath;
                for (File file:db.getFiles()) {
                    filePath = file.getAbsolutePath();
                    JSONArray array = ReflectJava.getCrunchifyClassNamesFromJar(filePath);
                    List<String> lst = new ArrayList<>();

                    for (int i = 0; i < array.length(); i++) {
                        lst.add(array.getString(i));
                    }

                    items.addAll(lst);
                    list.setItems(items);
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });


        list.setOnMouseClicked(event -> {
            final ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString((list.getSelectionModel().getSelectedItem() == null ? "" : list.getSelectionModel().getSelectedItem()));
            Clipboard.getSystemClipboard().setContent(clipboardContent);
        });

        Pane stckPane = new StackPane();

        stckPane.prefHeightProperty().bind(scene.heightProperty());
        stckPane.prefWidthProperty().bind(scene.widthProperty());

        stckPane.getChildren().add(list);
        stckPane.autosize();

        root.getChildren().add(stckPane);


        primaryStage.getIcons().add(new Image("dilisim.jpg"));
        primaryStage.setTitle("Dilisim Class Finder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
