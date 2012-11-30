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

/**
 * View for showing credits for the Lumen game.
 * 
 * @author Jonas Hartmann &lt;jonasharty@gmail.com&gt;
 * @since 30.11.2012
 */
public class CreditsView extends UIScreen {

	private Root root;
	private Label label;
	private Button backButton;

	public CreditsView() {
		label = new Label("Jonas Hartmann");
		backButton = new Button("Back");
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
		root.add(label);
		root.add(backButton);
	}

	/**
	 * Called when a screen has been removed from the stack. This will always be
	 * preceeded by a call to {@link #wasHidden}, though not always immediately.
	 */
	public void wasRemoved() {
		super.wasRemoved();
		iface.destroyRoot(root);
	}

	public void addBackButtonClickListener(final ClickListener listener) {
		SignalView<Button> clicked = backButton.clicked();
		clicked.connect(new UnitSlot() {
			public void onEmit() {
				listener.onClick();
			};
		});
	}
}
