import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ShakeStage {
    private int x = 0;
    private int y = 0;

    public void shakeStage(Stage primaryStage) {
        Timeline timelineX = new Timeline(new KeyFrame(Duration.seconds(0.036), t -> {
            if (x == 0) {
                primaryStage.setX(primaryStage.getX() + 10);
                x = 1;
            } else {
                primaryStage.setX(primaryStage.getX() - 10);
                x = 0;
            }
        }));

        timelineX.setCycleCount(10);
        timelineX.setAutoReverse(false);
        timelineX.play();


        Timeline timelineY = new Timeline(new KeyFrame(Duration.seconds(0.036), t -> {
            if (y == 0) {
                primaryStage.setY(primaryStage.getY() + 10);
                y = 1;
            } else {
                primaryStage.setY(primaryStage.getY() - 10);
                y = 0;
            }
        }));

        timelineY.setCycleCount(10);
        timelineY.setAutoReverse(false);
        timelineY.play();
    }
}
