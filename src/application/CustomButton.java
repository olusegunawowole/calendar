package application;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

public class CustomButton extends StackPane {
	private final double SIZE = 50;
	
	public CustomButton(ButtonType buttonType) {
		switch (buttonType) {
		case UP:
		case DOWN:
			createArrowButton(buttonType);
			break;
		case CALENDAR:
			createCalendarButton();
			break;
		case CLOCK:
			createClockButton();
		default:
			break;
		}

		setOnMouseEntered(e -> {
			setStyle("-fx-background-color: rgb(53, 53, 53)");
		});

		setOnMouseExited(e -> {
			setStyle("-fx-background-color: rgb(53, 53, 53, 0)");
		});

		setOnMousePressed(e -> {
			setStyle("-fx-background-color: rgb(53, 53, 53, 0.8)");
		});

		setOnMouseReleased(e -> {
			setStyle("-fx-background-color: rgb(53, 53, 53)");
		});

		setMinSize(SIZE, SIZE);
		setMaxSize(SIZE, SIZE);
		disableProperty().addListener((obj, oldVal, newVal)->{
			if(newVal) {
				setOpacity(.5);
			}
			else {
				setOpacity(1);
			}
		});
	}
	
	private void createClockButton() {
		Circle circle = new Circle(12.5);
		circle.setStroke(Color.rgb(157, 157, 157));
		circle.setFill(null);
		circle.setStrokeWidth(2);
		
		// Create a polyline to display clock hands
		Polyline polyline = new Polyline(0.0, 0.0, 0.0, 10.0, 5.0, 10.0);
		polyline.setStroke(Color.rgb(157, 157, 157));
		polyline.setStrokeWidth(2);
		polyline.translateXProperty().set(2);
		polyline.translateYProperty().set(-3);
		getChildren().addAll(circle, polyline);
		
	}
	
	private void createCalendarButton() {
		Label label = new Label("1");
		label.setWrapText(true);
		label.setAlignment(Pos.CENTER);
		label.setMaxSize(25, 25);
		label.setStyle("-fx-text-fill: rgb(157, 157, 157); -fx-border-width: 2px; -fx-border-color: rgb(157, 157, 157)");
		getChildren().add(label);
	}

	private void createArrowButton(ButtonType buttonType) {
		Polygon triangle;
		if (ButtonType.UP == buttonType) {
			triangle = new Polygon(13.0, 10.0, 20.0, 0.0, 27.0, 10.0);
		} else {
			triangle = new Polygon(13.0, 0.0, 20.0, 10.0, 27.0, 0.0);
		}
		triangle.setFill(Color.rgb(157, 157, 157));
		getChildren().add(triangle);
	}
}
