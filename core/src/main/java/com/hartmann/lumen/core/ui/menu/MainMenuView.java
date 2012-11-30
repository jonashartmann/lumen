package com.hartmann.lumen.core.ui.menu;

import static playn.core.PlayN.graphics;
import playn.core.Font;
import react.SignalView;
import react.UnitSlot;
import tripleplay.game.UIScreen;
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;

import com.hartmann.lumen.core.tools.ClickListener;

public class MainMenuView extends UIScreen {

	protected Label label;
	protected Button singlePlayerButton;
	protected Button creditsButton;
	protected Root root;

	public MainMenuView() {
		label = new Label("Lumen Lux");
		singlePlayerButton = new Button("Single Player");
		creditsButton = new Button("Credits");
	}

	/** Called when a screen is added to the screen stack for the first time. */
	public void wasAdded() {
		super.wasAdded();
	}

	/**
	 * Called when a screen becomes the top screen, and is therefore made
	 * visible.
	 */
	public void wasShown() {
		super.wasShown();
		root = iface.createRoot(AxisLayout.vertical().gap(15),
				SimpleStyles.newSheet(), layer);
		root.addStyles(Style.BACKGROUND.is(Background.bordered(0xFFCCCCCC,
				0xFF99CCFF, 5).inset(5, 10)));
		root.setSize(width(), height());

		root.add(label.addStyles(Style.FONT.is(graphics().createFont(
				"Helvetica", Font.Style.PLAIN, 24))));
		root.add(singlePlayerButton);
		root.add(creditsButton);
	}

	/**
	 * Called when a screen is no longer the top screen (having either been
	 * pushed down by another screen, or popped off the stack).
	 */
	public void wasHidden() {
		super.wasHidden();
	}

	/**
	 * Called when a screen has been removed from the stack. This will always be
	 * preceeded by a call to {@link #wasHidden}, though not always immediately.
	 */
	public void wasRemoved() {
		super.wasRemoved();
		iface.destroyRoot(root);
	}

	/**
	 * Called when this screen's transition into view has completed.
	 * {@link #wasShown} is called immediately before the transition begins, and
	 * this method is called when it ends.
	 */
	public void showTransitionCompleted() {
	}

	/**
	 * Called when this screen's transition out of view has started.
	 * {@link #wasHidden} is called when the hide transition completes.
	 */
	public void hideTransitionStarted() {
	}

	public void addSinglePlayerButtonClickListener(final ClickListener listener) {
		SignalView<Button> clicked = singlePlayerButton.clicked();
		clicked.connect(new UnitSlot() {
			public void onEmit() {
				listener.onClick();
			};
		});
	}

	public void addCreditsButtonClickListener(final ClickListener listener) {
		SignalView<Button> clicked = creditsButton.clicked();
		clicked.connect(new UnitSlot() {
			public void onEmit() {
				listener.onClick();
			};
		});
	}
}
