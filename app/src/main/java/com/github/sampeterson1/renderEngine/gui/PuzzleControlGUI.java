/*
 *	Stickers Twisty Puzzle Simulator and Solver
 *	Copyright (C) 2022 Sam Peterson <sam.peterson1@icloud.com>
 *	
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.sampeterson1.renderEngine.gui;

import java.util.Collection;

import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.puzzle.display.PuzzleMaster;
import com.github.sampeterson1.renderEngine.text.Font;
import com.github.sampeterson1.renderEngine.text.FontUtil;
import com.github.sampeterson1.renderEngine.text.GUIText;

public class PuzzleControlGUI implements GUIEventListener {
	
	public PuzzleControlGUI() {
		GUIMaster.addEventListener(this);
		
		Collection<String> puzzleNames = PuzzleMaster.getPuzzleNames();
		String[] dropdownOptions = puzzleNames.toArray(new String[0]);
		Font arial = new Font("arial.fnt", "arial.png", 0.2f);
		
		GUIComponent container = new GUIComponent("container", 0, 0.6f, 1f, 0.4f);
		
		GUIDropdownBox puzzleSelecter = new GUIDropdownBox(container, "Puzzle Selecter", 0.025f, 0.95f, 0.2f, 0.1f);
		puzzleSelecter.setParent(container);
		puzzleSelecter.createOptions(dropdownOptions, arial);
		
		float yOff = 0.5f * FontUtil.getScaledLineHeight(arial) / container.getAbsoluteHeight();
		new GUIText(container, "Size Label", "Size: ", arial, 0.25f, 0.9f + yOff);
	
		GUITextBox sizeTextBox = new GUITextBox(container, "Size Text Box", arial, 0.32f, 0.84f, 0.06f);
		sizeTextBox.setParent(container);
		
		new GUIText(container, "Animation Speed Label", "Animation Speed: ", arial, 0.4f, 0.9f + yOff);
		new GUISlider(container, "Animation Speed Slider", 0.63f, 0.85f, 0.32f);
		
		new GUIText(container, "Algorithm Label", "Algorithm: ", arial, 0.02f, 0.7f + yOff);
		new GUITextBox(container, "Algorithm Text Box", arial, 0.15f, 0.65f, 0.4f);
		
		GUIButton executeButton = new GUIButton(container, "Execute Button", 0.6f, 0.64f, 0.17f, 3.5f);
		executeButton.createLabel("Execute", arial);
		executeButton.setBaseColor(new Vector3f(0, 0, 1));
		executeButton.setHighlightColor(new Vector3f(0.3f, 0.3f, 1f));
		executeButton.setShadowColor(new Vector3f(0, 0, 0.8f));
		
		new GUIText(container, "Scramble Length Label", "Scramble Length: ", arial, 0.02f, 0.52f + yOff);
		new GUITextBox(container, "Scramble Length Text Box", arial, 0.23f, 0.47f, 0.1f);
		
		GUIButton scrambleButton = new GUIButton(container, "Scramble Button", 0.37f, 0.46f, 0.17f, 3.5f);
		scrambleButton.setParent(container);
		scrambleButton.createLabel("Scramble", arial);
		scrambleButton.setBaseColor(new Vector3f(1, 0, 0));
		scrambleButton.setHighlightColor(new Vector3f(1, 0.3f, 0.3f));
		scrambleButton.setShadowColor(new Vector3f(0.8f, 0, 0));
		
		
		GUIButton solveButton = new GUIButton(container, "Solve Button", 0.6f, 0.46f, 0.17f, 3.5f);
		solveButton.setParent(container);
		solveButton.createLabel("Solve", arial);
		solveButton.setBaseColor(new Vector3f(0, 1, 0));
		solveButton.setHighlightColor(new Vector3f(0.3f, 1f, 0.3f));
		solveButton.setShadowColor(new Vector3f(0, 0.8f, 0));

		PuzzleMaster.selectPuzzle(dropdownOptions[0]);
	}
	
	@Override
	public void handleEvent(GUIEvent e) {
		GUIEventType type = e.getType();
		GUIComponent component = e.getComponent();
		String name = component.getName();
		
		if(type == GUIEventType.DROPDOWN_SELECTED) {
			GUIDropdownBox dropdownBox = (GUIDropdownBox) component;
			
			if(name.equals("Puzzle Selecter")) {
				PuzzleMaster.selectPuzzle(dropdownBox.getSelection());
			}
		} else if(type == GUIEventType.BUTTON_RELEASE) {
			if(name.equals("Scramble Button")) {
				PuzzleMaster.scramble();
			} else if(name.equals("Solve Button")) {
				PuzzleMaster.solve();
			} else if(name.equals("Execute Button")) {
				GUITextBox algorithmBox = (GUITextBox) GUIMaster.getComponent("Algorithm Text Box");
				String algorithm = algorithmBox.getString();
				PuzzleMaster.executeAlgorithm(algorithm);
			}
		} else if(type == GUIEventType.TEXT_BOX_UPDATE) {
			GUITextBox textBox = (GUITextBox) component;
			if(name.equals("Size Text Box")) {
				int size = Integer.parseInt(textBox.getString());
				PuzzleMaster.setPuzzleSize(size);
			} else if(name.equals("Scramble Length Text Box")) {
				int length = Integer.parseInt(textBox.getString());
				PuzzleMaster.setScrambleLength(length);
			}
		} else if(type == GUIEventType.SLIDER_MOVED) {
			GUISlider slider = (GUISlider) component;
			if(name.equals("Animation Speed Slider")) {
				PuzzleMaster.setAnimationSpeed(slider.getValue() * 100);
			}
		}
	}

}
