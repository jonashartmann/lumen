package com.hartmann.lumen.core.ui;

import static playn.core.PlayN.graphics;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Font.Style;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.TextFormat;
import playn.core.TextFormat.Alignment;
import playn.core.TextLayout;

import com.hartmann.lumen.core.model.LuxPlayer;

public class GameInformationView {

	private GroupLayer groupLayer;
	private Canvas canvas;
	private float screenWidth;
	private float screenHeight;

	public GameInformationView() {
	}

	public void init(float screenWidth, float screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		groupLayer = graphics().createGroupLayer();
		CanvasImage image = graphics().createImage(screenWidth, 100f);
		canvas = image.canvas();
		ImageLayer imageLayer = graphics().createImageLayer(image);
		groupLayer.add(imageLayer);
		graphics().rootLayer().add(groupLayer);
	}

	public void paint(float alpha, LuxPlayer currentPlayer) {
		drawPlayerInfo(currentPlayer);
	}

	private void drawPlayerInfo(LuxPlayer player) {
		TextLayout layoutText = graphics().layoutText(
				getPlayerName(player),
				new TextFormat().withAlignment(Alignment.LEFT).withFont(
						graphics().createFont("Times New Roman", Style.PLAIN,
								30f)));
		canvas.clear();
		canvas.setFillColor(player.getColor());
		float x = (screenWidth / 2) - (layoutText.width() / 2);
		canvas.fillText(layoutText, x, 0);
	}

	private String getPlayerName(LuxPlayer player) {
		switch (player) {
		case NEUTRAL:
			return "Neutral";
		case PLAYER_ONE:
			return "Player one";
		case PLAYER_TWO:
			return "Player two";
		default:
			return "NO PLAYER!";
		}
	}
}
