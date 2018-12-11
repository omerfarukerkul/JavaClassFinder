import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DragDrop extends Application {
    private ShakeStage shakeStage;
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 550, 400);
        ListView<String> list = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();

        /*Dragging over surface*/
        scene.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                event.consume();
            }
        });

        /*Dropping over surface*/
        scene.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                String filePath;
                List<String> lst = new ArrayList<>();
                for (File file : db.getFiles()) {
                    filePath = file.getAbsolutePath();
                    JSONArray array = ReflectJava.getCrunchifyClassNamesFromJar(filePath);

                    for (int i = 0; i < array.length(); i++) {
                        lst.add(array.getString(i));
                    }

                }
                Collections.sort(lst);
                items.addAll(lst);
                if (items.size() > 100) {
                    items.clear();
                    lst.clear();
                    list.setItems(items);
                }
                list.setItems(items);
            }
            event.setDropCompleted(success);
            shakeStage = new ShakeStage();
            shakeStage.shakeStage(primaryStage);
            event.consume();
        });

        /*Copy to Clipboard*/
        list.setOnMouseClicked(event -> {
            final ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString((list.getSelectionModel().getSelectedItem() == null ? "" : list.getSelectionModel().getSelectedItem()));
            Clipboard.getSystemClipboard().setContent(clipboardContent);
        });

        /*Tooltip*/
        list.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                final Label leadLbl = new Label();
                final Tooltip tooltip = new Tooltip();
                final ListCell<String> cell = new ListCell<String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            leadLbl.setText(item);
                            setText(item);
                            tooltip.setText(item);
                            setTooltip(tooltip);
                        }
                    }
                };
                return cell;
            }
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
