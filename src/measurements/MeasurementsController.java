/*
 * Copyright 2017 DSATool team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package measurements;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import dsatool.resources.ResourceManager;
import dsatool.util.Util;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import jsonant.value.JSONObject;

public class MeasurementsController {

	@FXML
	private final TabPane tabs = new TabPane();

	public MeasurementsController() {
		tabs.getStyleClass().add("background");
	}

	private void fillTab(final Tab tab, final JSONObject measurement) {
		final List<Label> labels = new ArrayList<>();
		final List<Double> factors = new ArrayList<>();
		final double[] currentFactor = new double[1];
		final DecimalFormat d = new DecimalFormat("#,###.##########", Util.decimalFormatSymbols);

		final Consumer<Double> update = newValue -> {
			for (int i = 0; i < labels.size(); ++i) {
				labels.get(i).setText(d.format(newValue * currentFactor[0] / factors.get(i)));
			}
		};

		final VBox box = new VBox(2);
		box.getStyleClass().add("background");
		tab.setContent(box);

		final HBox inputBox = new HBox(2);

		final TextField input = new TextField("1");
		inputBox.getChildren().add(input);
		input.textProperty().addListener((observable, oldValue, newValue) -> {
			if ("".equals(newValue)) {
				input.setText("0");
				update.accept(0d);
			} else {
				try {
					final Double val = d.parse(newValue).doubleValue();
					update.accept(val);
				} catch (final ParseException e) {
					input.setText(oldValue);
				}
			}
		});

		final Label inputUnit = new Label();
		inputBox.getChildren().add(inputUnit);

		box.getChildren().add(inputBox);

		boolean first = true;

		for (final String categoryName : measurement.keySet()) {
			box.getChildren().add(new Separator());
			box.getChildren().add(new Label(categoryName));

			final TilePane pane = new TilePane(2, 2);
			box.getChildren().add(pane);

			final JSONObject category = measurement.getObj(categoryName);
			for (final String unitName : category.keySet()) {
				final HBox unitBox = new HBox(2);
				unitBox.setAlignment(Pos.CENTER);

				final double factor = category.getDouble(unitName);

				final Button unitButton = new Button(unitName);
				unitButton.setOnAction(event -> {
					inputUnit.setText(unitName);
					currentFactor[0] = factor;
					update.accept(Double.parseDouble(input.getText()));
				});
				unitButton.setMinWidth(150);
				unitButton.setMaxWidth(Double.POSITIVE_INFINITY);
				HBox.setHgrow(unitButton, Priority.ALWAYS);
				unitBox.getChildren().add(unitButton);

				final Label unitLabel = new Label();
				unitLabel.setPrefWidth(150);
				unitBox.getChildren().add(unitLabel);

				pane.getChildren().add(unitBox);

				labels.add(unitLabel);
				factors.add(factor);

				if (first) {
					first = false;
					currentFactor[0] = factor;
					inputUnit.setText(unitName);
				}
			}
		}
		update.accept(1d);
	}

	public Node getPane() {
		return tabs;
	}

	public void load() {
		final JSONObject units = ResourceManager.getResource("data/Masse");

		tabs.getTabs().clear();

		for (final String measurementName : units.keySet()) {
			final Tab tab = new Tab(measurementName);
			tab.setClosable(false);
			fillTab(tab, units.getObj(measurementName));
			tabs.getTabs().add(tab);
		}
	}
}
