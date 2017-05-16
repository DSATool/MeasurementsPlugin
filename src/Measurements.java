
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
import dsatool.gui.Main;
import dsatool.plugins.Plugin;
import measurements.MeasurementsController;

/**
 * A plugin for providing conversion between different units of measurement
 *
 * @author Dominik Helm
 */
public class Measurements extends Plugin {
	/**
	 * The composite for this plugin
	 */
	private MeasurementsController controller;

	/*
	 * (non-Javadoc)
	 *
	 * @see plugins.Plugin#getPluginName()
	 */
	@Override
	public String getPluginName() {
		return "Measurements";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see plugins.Plugin#initialize()
	 */
	@Override
	public void initialize() {
		Main.addDetachableToolComposite("DSA", "Maße", 300, 200, () -> {
			getNotifications = true;

			controller = new MeasurementsController();
			controller.load();

			return controller.getPane();
		});
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see plugins.Plugin#load()
	 */
	@Override
	public void load() {
		if (controller != null) {
			controller.load();
		}
	}
}
