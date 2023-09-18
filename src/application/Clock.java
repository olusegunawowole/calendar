package application;

import java.time.LocalDateTime;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Clock {
	private Canvas canvas;
	private GraphicsContext g;
	private double size;
	private Color backgroundColor;
	private Color fillColor;
	private Color secondPointerColor;
	private double cx, cy; // centerX, centerY - Center of the canvas.
	private Label digitalClockLabel;
	private String dateAsString;
	private double degreeHour, degreeMinute, degreeSecond;
	private AnimationTimer timer;
	private final String[] DAYS = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	private final String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
			"Dec" };

	public Clock() {
		size = 400;
		canvas = new Canvas(size, size);
		g = canvas.getGraphicsContext2D();
		digitalClockLabel = new Label("12:00:00 AM");
		digitalClockLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		digitalClockLabel.setPrefHeight(50);
		digitalClockLabel.setStyle("-fx-text-fill: #FFFFFF");
		backgroundColor = Color.BLACK;
		fillColor = Color.WHITE;
		secondPointerColor = Color.RED;
		cx = size * 0.5;
		cy = size * 0.5;
		draw();
		timer = new AnimationTimer() {
			int frame = 0;

			@Override
			public void handle(long now) {
				if (frame % 60 == 0) {
					setClock();
				}
				frame++;
			}
		};
		timer.start();
	}

	private void draw() {
		g.clearRect(0, 0, size, size);
		g.setFill(backgroundColor);
		g.fillOval(0, 0, size, size);
		g.setLineWidth(2);
		g.setStroke(fillColor);
		g.strokeOval(0.5, 0.5, size - 1, size - 1);
		g.setFill(fillColor);
		g.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.ITALIC, relativeToSize(0.05)));
		g.fillText(dateAsString, cx - relativeToSize(0.23), cy - relativeToSize(0.1875));
		drawClock();
		drawPointer(degreeHour, relativeToSize(0.27), fillColor);
		drawPointer(degreeMinute, relativeToSize(0.375), fillColor);
		drawPointer(degreeSecond, relativeToSize(0.375), secondPointerColor);

	}

	private void drawClock() {
		double w = relativeToSize(0.0125);
		double h = relativeToSize(0.0125);
		int dotNum = 60;
		double radius = (this.size) * .5 - relativeToSize(0.0375);
		double slice = (360 / dotNum);
		double x, y;
		// String[] romanNumerals = { "XII", "I", "II", "III", "IV", "V", "VI", "VII",
		// "VIII", "IX", "X", "XI" };
		for (int i = 0; i < dotNum; i++) {
			double angle = slice * i;
			x = cx + radius * Math.sin(toRadian(angle));
			y = cy + radius * Math.cos(toRadian(angle));
			g.save();
			g.setFill(fillColor);
			g.translate(x, y);
			g.rotate(-angle);
			g.fillOval(0, 0, w, h);
			g.restore();
		}
		radius = (this.size) * .5 - relativeToSize(0.075);
		for (int i = 0; i < 12; i++) {
			double angle = 360 / 12 * i;
			x = cx + radius * Math.sin(toRadian(angle));
			y = cy + radius * Math.cos(toRadian(angle));
			g.save();
			g.setFill(fillColor);
			g.translate(x, y);
			g.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.ITALIC, relativeToSize(0.04)));
			int hr = (6 - i) % 12;
			hr = hr <= 0 ? hr + 12 : hr;
			if (hr == 12)
				g.fillText(hr + "", -relativeToSize(0.03), relativeToSize(0.01));
			else
				g.fillText(hr + "", -relativeToSize(0.01), relativeToSize(0.01));

			// Comment the previous line and uncomment romanNumerals above and the following
			// lines to use roman numerals

			// hr = hr % 12;
			// if (hr == 0 || hr == 3) {
			// g.fillText(romanNumerals[hr], -relativeToSize(0.04), relativeToSize(0.01));
			// } else {
			// g.fillText(romanNumerals[hr], -relativeToSize(0.01), relativeToSize(0.01));
			// }

			g.restore();

		}

	}

	public void drawPointer(double degree, double length, Color color) {
		double radian = toRadian(degree);
		double x1, y1;
		g.setStroke(color);
		x1 = cx + Math.cos(radian) * length;
		y1 = cy + Math.sin(radian) * length;
		g.setLineWidth(relativeToSize(0.0125));
		g.strokeLine(cx, cy, x1, y1);
		g.setFill(Color.RED);
		g.fillOval(cx - relativeToSize(0.025), cy - relativeToSize(0.025), relativeToSize(0.05), relativeToSize(0.05));

	}

	private void setClock() {
		LocalDateTime now = LocalDateTime.now();
		int hour = now.getHour();
		int minute = now.getMinute();
		int second = now.getSecond();
		String day = DAYS[now.getDayOfWeek().getValue()%7];
		String month = MONTHS[now.getMonthValue() - 1];
		degreeHour = (360 * hour / 12) + (30 * minute / 60.0) - 90;
		degreeMinute = (360 * minute / 60) + (second / 60.0 * 6) - 90;
		degreeSecond = (360 * second / 60.0) - 90;
		dateAsString = day + "., " + month + " " + now.getDayOfMonth() + ", " + now.getYear();
		String AM_PM = hour > 12 ? "PM" : "AM";
		hour = hour > 12 ? hour % 12 : hour;
		String hourString = hour < 10 ? "0" + hour : "" + hour;
		String minuteString = minute < 10 ? "0" + minute : "" + minute;
		String secondString = second < 10 ? "0" + second : "" + second;
		String time = hourString + ":" + minuteString + ":" + secondString + " " + AM_PM;
		digitalClockLabel.setText(time);
		draw();
	}

	// Setters and getters
	public void setSize(double size) {
		if (size < 50)
			return;
		this.size = size;
		canvas.setWidth(size);
		canvas.setHeight(size);
		cx = size * 0.5;
		cy = size * 0.5;
		draw();
	}

	public double getSize() {
		return size;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public Color getSecondPointerColor() {
		return secondPointerColor;
	}

	public void setSecondPointerColor(Color secondPointerColor) {
		this.secondPointerColor = secondPointerColor;
	}

	public Label getDigitalClockLabel() {
		return digitalClockLabel;
	}

	// Utility methods
	private double toRadian(double degree) {
		return Math.PI / 180 * degree;
	}

	private double relativeToSize(double proportion) {
		return size * proportion;
	}
}
