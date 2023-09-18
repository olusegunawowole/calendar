package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CustomLabel extends StackPane {
	private boolean selected; // true if the label contains today's date or current month and false if otherwise.
	private boolean grayed; // indicates if font color should be grayed out or not
	private boolean highlighted; // true if the label is highlighted.
	private Label label; // it contains text.

	public CustomLabel(String value, double width, double height, boolean selectable) {
		label = new Label(value);
		label.setPrefSize(width, height);
		label.setFont(Font.font("Arial", FontWeight.NORMAL, 17));
		label.setAlignment(Pos.CENTER);
		label.setStyle(
				"-fx-text-fill: #FFFFFF; -fx-border-width: 1px; -fx-border-color: transparent; -fx-border-radius: 50%");
		getChildren().add(label);
		setStyle(
				"-fx-border-width: 1px; -fx-border-color: transparent; -fx-border-radius: 50%; -fx-background-radius: 50%");
		setPadding(new Insets(2));
		if (selectable) {
			setOnMouseEntered(e -> {
				if (selected) {
					setOpacity(0.8);

				} else if (highlighted) {
					setStyle(
							"-fx-background-color: rgb(53, 53, 53); -fx-border-width: 1px; -fx-border-color: #4CC2FF; -fx-border-radius: 50%; -fx-background-radius: 50%");
				} else {
					setStyle(
							"-fx-background-color: rgb(53, 53, 53); -fx-border-width: 1px; -fx-border-color: transparent; -fx-border-radius: 50%; -fx-background-radius: 50%");
				}
			});

			setOnMouseExited(e -> {
				if (selected) {
					setOpacity(1.0);

				} else if (highlighted) {
					setStyle(
							"-fx-background-color: rgb(53, 53, 53, 0); -fx-border-width: 1px; -fx-border-color: #4CC2FF; -fx-border-radius: 50%; -fx-background-radius: 50%");
				} else {
					setStyle(
							"-fx-background-color: rgb(53, 53, 53, 0); -fx-border-width: 1px; -fx-border-color: transparent; -fx-border-radius: 50%; -fx-background-radius: 50%");
				}

			});
		}
	}

	public CustomLabel(int value, double width, double height, boolean selectable) {
		this(value + "", width, height, selectable);
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		label.setText(value + "");
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		label.setText(value);
	}

	public void setSelected() {
		setSelected(true);
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the today to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
		if (true) {
			setStyle(
					"-fx-background-color: #4CC2FF; -fx-border-width: 1px; -fx-border-color: #4CC2FF; -fx-border-radius: 50%; -fx-background-radius: 50%");
			label.setStyle(
					"-fx-text-fill: #000000; -fx-border-width: 1px; -fx-border-color: transparent; -fx-border-radius: 50%");
		}

	}

	/**
	 * @return the grayed
	 */
	public boolean isGrayed() {
		return grayed;
	}

	/**
	 * @param grayed the grayed to set
	 */
	public void setGrayed(boolean grayed) {
		this.grayed = grayed;	
	}

	public void grayOut() {
		if(selected || highlighted)
			return;
		if (grayed) 
			label.setStyle(
					"-fx-text-fill: #808080; -fx-border-width: 1px; -fx-border-color: transparent; -fx-border-radius: 50%");
		else {
			label.setStyle(
					"-fx-text-fill: #FFFFFF; -fx-border-width: 1px; -fx-border-color: transparent; -fx-border-radius: 50%");
			}
	}

	/**
	 * @return the highlighted
	 */
	public boolean isHighlighted() {
		return highlighted;
	}

	/**
	 * @param highlighted the highlighted to set
	 */
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
		if (highlighted) {
			if (selected) {
				setStyle(
						"-fx-background-color: transparent; -fx-border-width: 1px; -fx-border-color: #4CC2FF; -fx-border-radius: 50%; -fx-background-radius: 50%");
				label.setStyle(
						"-fx-text-fill: #000000; -fx-background-color: #4CC2FF; -fx-background-radius: 50%; -fx-border-width: 1px; -fx-border-color: transparent; -fx-border-radius: 50%");
			} else {
				setStyle(
						"-fx-background-color: rgb(53, 53, 53, 0.8); -fx-border-width: 1px; -fx-border-color: #4CC2FF; -fx-border-radius: 50%; -fx-background-radius: 50%");
				label.setStyle(
						"-fx-text-fill: #4CC2FF; -fx-border-width: 1px; -fx-border-color: transparent; -fx-border-radius: 50%");
			}
		} else {
			if (selected) {
				setStyle(
						"-fx-background-color: #4CC2FF; -fx-border-width: 1px; -fx-border-color: #4CC2FF; -fx-border-radius: 50%; -fx-background-radius: 50%");
				label.setStyle(
						"-fx-text-fill: #000000; -fx-border-width: 1px; -fx-border-color: transparent; -fx-border-radius: 50%");
				return;
			}

			setStyle(
					"-fx-background-color: rgb(53, 53, 53, 0.0); -fx-border-width: 1px; -fx-border-color: transparent; -fx-border-radius: 50%; -fx-background-radius: 50%");
			if (grayed)
				label.setStyle(
						"-fx-text-fill: #808080; -fx-border-width: 1px; -fx-border-color: transparent; -fx-border-radius: 50%");
			else
				label.setStyle(
						"-fx-text-fill: #FFFFFF; -fx-border-width: 1px; -fx-border-color: transparent; -fx-border-radius: 50%");
		}
	}

	public void clear() {
		label.setText(null);
		selected = false;
		grayed = false;
		highlighted = false;
		label.setStyle(
				"-fx-text-fill: #FFFFFF; -fx-border-width: 1px; -fx-border-color: transparent; -fx-border-radius: 50%");
		setStyle(
				"-fx-border-width: 1px; -fx-border-color: transparent; -fx-border-radius: 50%; -fx-background-radius: 50%");

	}
}
