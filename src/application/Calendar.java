package application;

import java.time.LocalDate;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Calendar extends Application {
	private CustomButton downButton, upButton;
	private Clock clock;
	private Stage stage;
	private Label calendarAndClockLabel;
	private Label calendarOptionLabel;
	private VBox root, calendarPane, calendarAndControlPane;
	private StackPane calendarOptionPane, calendarBottomPane, calendarRoot;
	private boolean displayCalendar = true; // True if calendar is displayed or false if otherwise.
	private String dateAsString = "Friday, 25 August";
	private String monthAndYearAsString;
	private final String[] MONTHS = { "January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };
	private LocalDate selectedDate;
	private final double SIZE = 7;
	private IntegerProperty yearProperty;
	private int selectedMonth;
	private int decadeIndex;
	private int monthIndex = 0; // 0 = current month, < 0 = previous months & > 0 = next months
	private int option; // To be used for calendar selection. 0 = calendar view; 1 = months in a year; 2
						// = years (decade)

	@Override
	public void start(Stage primaryStage) {
		try {
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.sizeToScene();
			primaryStage.setTitle("Desktop Calendar");
			primaryStage.setResizable(false);
			primaryStage.show();
			stage = primaryStage;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void init() throws Exception {
		clock = new Clock();
		clock.setSize(450);
		clock.setBackgroundColor(Color.web("#292929"));
		String[] days = { "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" };
		GridPane dayOfWeekHeaderPane = new GridPane();
		dayOfWeekHeaderPane.setHgap(5);
		dayOfWeekHeaderPane.setVgap(5);
		dayOfWeekHeaderPane.setPadding(new Insets(5));
		for (int col = 0; col < SIZE; col++) {
			CustomLabel label = new CustomLabel(days[col], 55, 55, false);
			dayOfWeekHeaderPane.add(label, col, 0);
		}
		LocalDate date = LocalDate.now();
		GridPane gridPane = createCalendarViewPane(date);
		grayOut(gridPane);
		calendarBottomPane = new StackPane(gridPane);
		dateAsString = toProperCase(date.getDayOfWeek().toString()) + ", " + date.getDayOfMonth() + " "
				+ MONTHS[date.getMonthValue() - 1];

		monthAndYearAsString = MONTHS[date.getMonthValue() - 1] + ", " + date.getYear();
		calendarOptionLabel = new Label(monthAndYearAsString);
		calendarOptionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		calendarOptionLabel.setPrefHeight(50);
		calendarOptionLabel.setStyle("-fx-text-fill: #FFFFFF");
		yearProperty = new SimpleIntegerProperty(date.getYear());
		selectedMonth = date.getMonthValue();
		decadeIndex = Decade.getIndex(date.getYear());

		configureCalendarOptionPane();

		upButton = new CustomButton(ButtonType.UP);
		upButton.setOnMouseClicked(e -> {
			if (option == 0) {
				setCalendar(false);
			} else if (option == 1) {
				setMonth(false);
			} else {
				setYear(false);
			}
		});
		downButton = new CustomButton(ButtonType.DOWN);
		downButton.setOnMouseClicked(e -> {
			if (option == 0) {
				setCalendar(true);
			} else if (option == 1) {
				setMonth(true);
			} else {
				setYear(true);
			}
		});

		HBox.setHgrow(calendarOptionPane, Priority.ALWAYS);
		HBox controlPane = new HBox(7, calendarOptionPane, upButton, downButton);
		controlPane.setPadding(new Insets(0, 10, 0, 10));

		calendarPane = new VBox(dayOfWeekHeaderPane, calendarBottomPane);
		VBox.setMargin(controlPane, new Insets(20, 0, 0, 0));

		calendarRoot = new StackPane(calendarPane);

		calendarAndControlPane = new VBox(controlPane, calendarRoot);

		calendarAndClockLabel = new Label(dateAsString);
		calendarAndClockLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		calendarAndClockLabel.setPrefHeight(50);
		calendarAndClockLabel.setStyle("-fx-text-fill: #FFFFFF");

		CustomButton calendarButton = new CustomButton(ButtonType.CALENDAR);
		CustomButton clockButton = new CustomButton(ButtonType.CLOCK);

		StackPane pane = new StackPane(calendarAndClockLabel);
		pane.setAlignment(Pos.TOP_LEFT);
		pane.setPadding(new Insets(0, 0, 0, 10));
		HBox.setHgrow(pane, Priority.ALWAYS);
		
		StackPane container = new StackPane(calendarAndControlPane);
		container.setPrefSize(473.0,548.0);
				
		StackPane buttonPane = new StackPane(clockButton);
		buttonPane.setOnMouseClicked(e -> {
			buttonPane.getChildren().clear();
			pane.getChildren().clear();
			container.getChildren().clear();
			if (displayCalendar) {
				buttonPane.getChildren().add(calendarButton);
				pane.getChildren().add(clock.getDigitalClockLabel());
				container.getChildren().add(clock.getCanvas());
				stage.setTitle("Clock");
				displayCalendar = false;
			} else {
				buttonPane.getChildren().add(clockButton);
				container.getChildren().add(calendarAndControlPane);
				pane.getChildren().add(calendarAndClockLabel);
				stage.setTitle("Desktop Calendar");
				displayCalendar = true;
			}
		});

		HBox topPane = new HBox(5, pane, buttonPane);
		topPane.setPadding(new Insets(10));
		topPane.setStyle("-fx-background-color: #212121");

		root = new VBox(topPane, container);
		root.setStyle("-fx-background-color: #292929");

		yearProperty.addListener(e -> {
			int year = yearProperty.get();
			decadeIndex = Decade.getIndex(year);
			upButton.setDisable(year == 1923 || decadeIndex <= 0);
			downButton.setDisable(year == 2123 || decadeIndex >= 19);
			if (option == 1) {
				calendarOptionLabel.setText(year + "");
			}

		});
	}

	private void setSlideAnimation(GridPane inPane, GridPane outPane, boolean next,
			EventHandler<ActionEvent> onFinished) {
		double width = outPane.getWidth();
		double height = outPane.getHeight();
		if (next) {
			Rectangle clipRectIn = new Rectangle(width, height);
			clipRectIn.translateYProperty().set(0);
			inPane.setClip(clipRectIn);
			inPane.translateYProperty().set(height);

			Rectangle clipRectOut = new Rectangle(width, height);
			clipRectOut.translateYProperty().set(0);
			outPane.setClip(clipRectOut);
			outPane.translateYProperty().set(0);

			KeyValue kvMoveUp1 = new KeyValue(clipRectIn.translateYProperty(), 0);
			KeyValue kvMoveUp2 = new KeyValue(inPane.translateYProperty(), 0);
			KeyValue kvMoveUp3 = new KeyValue(outPane.translateYProperty(), -height);
			KeyValue kvMoveUp4 = new KeyValue(clipRectOut.translateYProperty(), height);
			KeyFrame kfMoveUp = new KeyFrame(Duration.seconds(0.25), kvMoveUp1, kvMoveUp2, kvMoveUp3, kvMoveUp4);
			Timeline slideUp = new Timeline(kfMoveUp);

			slideUp.setOnFinished(onFinished);
			slideUp.play();
		} else {
			Rectangle clipRectIn = new Rectangle(width, height);
			clipRectIn.translateYProperty().set(height);
			inPane.setClip(clipRectIn);
			inPane.translateYProperty().set(-height);

			Rectangle clipRectOut = new Rectangle(width, height);
			clipRectOut.translateYProperty().set(0);
			outPane.setClip(clipRectOut);
			outPane.translateYProperty().set(0);

			KeyValue kvMoveDown1 = new KeyValue(clipRectIn.translateYProperty(), -0);
			KeyValue kvMoveDown2 = new KeyValue(inPane.translateYProperty(), 0);
			KeyValue kvMoveDown3 = new KeyValue(outPane.translateYProperty(), height);
			KeyValue kvMoveDown4 = new KeyValue(clipRectOut.translateYProperty(), -height);
			KeyFrame kfMoveDown = new KeyFrame(Duration.seconds(0.25), kvMoveDown1, kvMoveDown2, kvMoveDown3,
					kvMoveDown4);
			Timeline slideDown = new Timeline(kfMoveDown);

			slideDown.setOnFinished(onFinished);
			slideDown.play();
		}

	}

	private void scaleAnimation(Pane pane, boolean scaleIn) {
		if (scaleIn) {
			pane.setScaleX(1.1);
			pane.setScaleY(1.1);
		} else {
			pane.setScaleX(0.7);
			pane.setScaleY(0.7);
		}
		ScaleTransition scale = new ScaleTransition(Duration.seconds(.15), pane);
		scale.setToX(1);
		scale.setToY(1);
		scale.play();
	}

	private GridPane createCalendarViewPane(LocalDate date) {
		// Set date to the first day of the month.
		date = date.minusDays(date.getDayOfMonth() - 1);

		// Get day of the week the first day is.
		// LocalDate begins counting day of the week from Monday
		// i.e. Monday = 1 while Sunday = 7. Using module of 7 will
		// make Sunday first day of the week with an index of 0;

		int firstDay = date.getDayOfWeek().getValue() % 7;
		int lastDay = date.getMonth().maxLength();
		int day = -firstDay;

		GridPane pane = new GridPane();
		pane.setHgap(5);
		pane.setVgap(5);
		pane.setPadding(new Insets(5));
		pane.setPrefSize(469.0, 403.0);

		// Set up the grid view
		int index = 0;
		for (int row = 0; row < SIZE - 1; row++) {
			for (int col = 0; col < SIZE; col++) {
				String str = date.plusDays(day).getDayOfMonth() + "";
				CustomLabel label = new CustomLabel(str, 55, 55, true);
				LocalDate selectedDate = date.plusDays(day);
				label.setOnMouseClicked(e -> {
					this.selectedDate = selectedDate;
					select(pane, label);
				});
				if (this.selectedDate != null && this.selectedDate.equals(selectedDate)) {
					label.setHighlighted(true);
				}

				if (day < 0 || index >= (firstDay + lastDay)) {
					label.setGrayed(true);
				}
				if (isToday(date.plusDays(day).getYear(), date.plusDays(day).getMonthValue(),
						date.plusDays(day).getDayOfMonth())) {
					label.setSelected(); // Highlight today
				}
				;
				pane.add(label, col, row); // Add label to pane
				index++;
				day++;
			}
		}
		return pane;
	}

	private GridPane createMonthViewPane() {
		GridPane pane = new GridPane();
		pane.setHgap(5);
		pane.setVgap(5);
		pane.setPadding(new Insets(5));
		pane.setPrefSize(469.0, 469.0);

		int index = 0;
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				String monthName = MONTHS[index % 12];
				monthName = monthName.substring(0, 3);
				CustomLabel label = new CustomLabel(monthName, 111, 111, true);
				int monthIndex = index + 1;
				if (monthIndex > 12) {
					label.setGrayed(true);
				}
				LocalDate now = LocalDate.now();
				if (now.getMonthValue() == monthIndex && now.getYear() == yearProperty.get())
					label.setSelected(true);
				label.setOnMouseClicked(e -> {
					if (monthIndex > 12) {
						selectedMonth = monthIndex % 12;
						yearProperty.set(yearProperty.get() + 1);
					} else {
						selectedMonth = monthIndex;
					}
					setCalendar();
				});
				pane.add(label, col, row);
				index++;
			}
		}
		return pane;
	}

	private GridPane createYearViewPane(Decade decade) {
		GridPane pane = new GridPane();
		pane.setHgap(5);
		pane.setVgap(5);
		pane.setPadding(new Insets(5));
		pane.setPrefSize(469.0, 469.0);

		ArrayList<Integer> yearList = decade.getDisplayRange();

		int index = 0;
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				if (index > yearList.size() - 1)
					break;
				int year = yearList.get(index);
				CustomLabel label = new CustomLabel(year, 111, 111, true);
				if (!decade.inDecade(year)) {
					label.setGrayed(true);
				}
				if (LocalDate.now().getYear() == year)
					label.setSelected(true);
				label.setOnMouseClicked(onYearClicked(year));
				pane.add(label, col, row);
				index++;
			}
		}
		return pane;
	}

	private EventHandler<MouseEvent> onYearClicked(int year) {
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				yearProperty.set(year);
				calendarOptionLabel.setText("" + yearProperty.get());
				GridPane pane = createMonthViewPane();
				calendarRoot.getChildren().clear();
				calendarOptionPane.setDisable(false);
				option = 1;
				calendarRoot.getChildren().add(pane);
				grayOut(pane);
				scaleAnimation(pane, false);
			}
		};
	}

	

	private void configureCalendarOptionPane() {
		calendarOptionPane = new StackPane(calendarOptionLabel);
		calendarOptionPane.setPadding(new Insets(0, 0, 0, 10));
		calendarOptionPane.setAlignment(Pos.TOP_LEFT);
		calendarOptionPane.setOnMouseEntered(e -> {
			calendarOptionPane.setStyle("-fx-background-color: rgb(53, 53, 53)");
		});
		calendarOptionPane.setOnMouseExited(e -> {
			calendarOptionPane.setStyle("-fx-background-color: rgb(53, 53, 53, 0)");
		});
		calendarOptionPane.setOnMousePressed(e -> {
			calendarOptionPane.setStyle("-fx-background-color: rgb(53, 53, 53, 0.8)");
		});
		calendarOptionPane.setOnMouseReleased(e -> {
			calendarOptionPane.setStyle("-fx-background-color: rgb(53, 53, 53)");
		});
		calendarOptionPane.setOnMouseClicked(e -> {
			option++;
			if (option == 2) {
				calendarOptionPane.setDisable(true);
			}
			if (option == 1) {
				calendarOptionLabel.setText("" + yearProperty.get());
				GridPane pane = createMonthViewPane();
				calendarRoot.getChildren().clear();
				new Thread() {
					public void run() {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Platform.runLater(() -> {
							calendarRoot.getChildren().add(pane);
							grayOut(pane);
							scaleAnimation(pane, true);
						});
					}
				}.start();

			} else if (option == 2) {
				calendarOptionLabel.setText(Decade.toString(yearProperty.get()));
				setYear();

			}
		});
	}

	private void select(GridPane pane, CustomLabel label) {
		if (label.isHighlighted()) { // unselect label if it is selected
			label.setHighlighted(false);
			selectedDate = null;
			return;
		}

		for (int index = 0; index < pane.getChildren().size(); index++) {
			CustomLabel customLabel = (CustomLabel) pane.getChildren().get(index);
			customLabel.setHighlighted(false);
		}
		label.setHighlighted(true);
	}

	private void grayOut(GridPane pane) {
		for (int index = 0; index < pane.getChildren().size(); index++) {
			CustomLabel customLabel = (CustomLabel) pane.getChildren().get(index);
			customLabel.grayOut();
		}
	}

	private boolean isToday(int year, int month, int dayOfMonth) {
		LocalDate today = LocalDate.now();
		boolean isToday = today.getYear() == year && today.getMonthValue() == month
				&& today.getDayOfMonth() == dayOfMonth;
		return isToday;
	}

	// This method changes to previous or next month for a given current month.
	private void setCalendar(boolean next) {
		if (next)
			monthIndex++;
		else
			monthIndex--;
		LocalDate date = LocalDate.now().plusMonths(monthIndex);
		GridPane inPane = createCalendarViewPane(date);
		GridPane outPane = (GridPane) calendarBottomPane.getChildren().get(0);
		calendarBottomPane.getChildren().add(inPane);

		monthAndYearAsString = MONTHS[date.getMonthValue() - 1] + ", " + date.getYear();
		calendarOptionLabel.setText(monthAndYearAsString);
		yearProperty.set(date.getYear());
		EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				calendarBottomPane.getChildren().clear();
				calendarBottomPane.getChildren().add(inPane);
				new Thread() { // To make gray-out effect visible
					public void run() {
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Platform.runLater(() -> {
							grayOut(inPane);
						});
					};
				}.start();
			}
		};
		setSlideAnimation(inPane, outPane, next, onFinished);
	}

	// This method set calendar for a given year and month.
	private void setCalendar() {
		// Setting month and year
		String monthString = selectedMonth < 10 ? "0" + selectedMonth : "" + selectedMonth;
		String dateStr = yearProperty.get() + "-" + monthString + "-01";
		LocalDate selectedDate = LocalDate.parse(dateStr);
		LocalDate today = LocalDate.now();
		// Find year difference between selected year and this year
		int yearsDiff = selectedDate.getYear() - today.getYear();
		int monthsDiff = selectedDate.getMonthValue() - today.getMonthValue();
		monthIndex = yearsDiff * 12 + monthsDiff;

		LocalDate date = LocalDate.now().plusMonths(monthIndex);
		GridPane pane = createCalendarViewPane(date);
		calendarBottomPane.getChildren().clear();
		calendarBottomPane.getChildren().add(pane);
		calendarRoot.getChildren().clear();
		option = 0;
		calendarOptionPane.setDisable(false);

		monthAndYearAsString = MONTHS[date.getMonthValue() - 1] + ", " + date.getYear();
		calendarOptionLabel.setText(monthAndYearAsString);
		new Thread() {
			public void run() {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Platform.runLater(() -> {
					calendarRoot.getChildren().add(calendarPane);
					grayOut(pane);
					scaleAnimation(calendarPane, false);
				});
			}
		}.start();

	}

	private void setMonth(boolean next) {
		if (next) {
			yearProperty.set(yearProperty.get() + 1);
		} else {
			yearProperty.set(yearProperty.get() - 1);
		}
		GridPane inPane = createMonthViewPane();
		GridPane outPane = (GridPane) calendarRoot.getChildren().get(0);
		calendarRoot.getChildren().add(inPane);
		EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				calendarRoot.getChildren().clear();
				calendarRoot.getChildren().add(inPane);
				new Thread() { // To make gray-out effect visible
					public void run() {
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Platform.runLater(() -> {
							grayOut(inPane);
						});
					};
				}.start();
			}
		};
		setSlideAnimation(inPane, outPane, next, onFinished);

	}

	private void setYear() {
		Decade decade = new Decade(decadeIndex);
		GridPane pane = createYearViewPane(decade);
		calendarRoot.getChildren().clear();
		new Thread() {
			public void run() {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Platform.runLater(() -> {
					calendarRoot.getChildren().add(pane);
					grayOut(pane);
					scaleAnimation(pane, true);
				});
			}
		}.start();
	}

	private void setYear(boolean next) {
		if (next) {
			decadeIndex++;
		} else {
			decadeIndex--;
		}
		upButton.setDisable(decadeIndex <= 0);
		downButton.setDisable(decadeIndex >= 19);
		Decade decade = new Decade(decadeIndex);
		calendarOptionLabel.setText(decade.toString());
		GridPane inPane = createYearViewPane(decade);
		GridPane outPane = (GridPane) calendarRoot.getChildren().get(0);
		calendarRoot.getChildren().add(inPane);
		EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				calendarRoot.getChildren().clear();
				calendarRoot.getChildren().add(inPane);
				new Thread() { // To make gray-out effect visible
					public void run() {
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Platform.runLater(() -> {
							grayOut(inPane);
						});
					};
				}.start();
			}
		};
		setSlideAnimation(inPane, outPane, next, onFinished);
	}

	private String toProperCase(String text) {
		StringBuilder sb = new StringBuilder();
		char[] arr = text.toCharArray();
		for (int index = 0; index < arr.length; index++) {
			char c = arr[index];
			if (index == 0) {
				sb.append(c);
			} else {
				sb.append(Character.toLowerCase(c));
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
