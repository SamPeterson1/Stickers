package com.github.sampeterson1.renderEngine.gui;

import java.util.Collection;

import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.puzzle.display.PuzzleMaster;
import com.github.sampeterson1.renderEngine.text.Font;

public class PuzzleControlGUI implements GUIEventListener {
	
	public PuzzleControlGUI() {
		GUIMaster.addEventListener(this);
		
		Collection<String> puzzleNames = PuzzleMaster.getPuzzleNames();
		String[] dropdownOptions = puzzleNames.toArray(new String[0]);
		Font arial = new Font("arial.fnt", "arial.png", 0.2f);
		
		GUIDropdownBox puzzleSelecter = new GUIDropdownBox("Puzzle Selecter", 0.05f, 0.9f, 0.2f, 0.03f);
		puzzleSelecter.createOptions(dropdownOptions, arial);
		
		GUIButton scrambleButton = new GUIButton("Scramble Button", 0.4f, 0.8f, 0.2f, 0.05f);
		scrambleButton.createLabel("Scramble", arial);
		scrambleButton.setBaseColor(new Vector3f(1, 0, 0));
		scrambleButton.setHighlightColor(new Vector3f(1, 0.3f, 0.3f));
		scrambleButton.setShadowColor(new Vector3f(0.8f, 0, 0));
		
		GUIButton solveButton = new GUIButton("Solve Button", 0.65f, 0.8f, 0.2f, 0.05f);
		solveButton.createLabel("Solve", arial);
		solveButton.setBaseColor(new Vector3f(0, 1, 0));
		solveButton.setHighlightColor(new Vector3f(0.3f, 1f, 0.3f));
		solveButton.setShadowColor(new Vector3f(0, 0.8f, 0));
		
		GUITextBox textBox = new GUITextBox("Test", arial, 0.05f, 0.5f, 0.4f);
		
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
			System.out.println(name);
			if(name.equals("Scramble Button")) {
				PuzzleMaster.scramble();
			} else if(name.equals("Solve Button")) {
				PuzzleMaster.solve();
			}
		}
	}

}
