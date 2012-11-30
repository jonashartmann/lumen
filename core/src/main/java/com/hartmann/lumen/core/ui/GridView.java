package com.hartmann.lumen.core.ui;

import static playn.core.PlayN.graphics;

import java.util.logging.Logger;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.GroupLayer;
import playn.core.ImageLayer;

import com.hartmann.lumen.core.model.HSBColor;
import com.hartmann.lumen.core.model.LuxGrid;
import com.hartmann.lumen.core.model.LuxGridPosition;
import com.hartmann.lumen.core.model.LuxPlayer;

/**
 * View that shows the board of the Lumen game.
 * 
 * @author Jonas Hartmann &lt;jonasharty@gmail.com&gt;
 * @since 30.11.2012
 */
public class GridView {
	private static final Logger logger = Logger.getLogger(GridView.class
			.getName());
	private static final int BLACK = Color.rgb(0, 0, 0);
	private static boolean debugOn = true;

	private int gridWidth;
	private int gridHeight;
	private int screenWidth;
	private int screenHeight;
	private ImageLayer[][] images;
	private Canvas canvas;
	private CanvasImage cimage;
	private LuxGrid grid;
	private GroupLayer rootGridLayer;
	private final float defaultOffsetX;
	private final float defaultOffsetY;

	public GridView(LuxGrid grid, int screenWidth, int screenHeight) {
		this(grid, screenWidth, screenHeight, 0, 0);
	}

	public GridView(LuxGrid grid, int screenWidth, int screenHeight,
			float offsetX, float offsetY) {
		this.grid = grid;
		this.gridWidth = grid.getWidth();
		this.gridHeight = grid.getHeight();
		this.screenWidth = screenWidth + (int) offsetX;
		this.screenHeight = screenHeight + (int) offsetY;
		cimage = graphics().createImage(this.screenWidth, this.screenHeight);
		canvas = cimage.canvas();
		images = new ImageLayer[gridWidth][gridHeight];
		rootGridLayer = graphics().createGroupLayer();

		this.defaultOffsetX = offsetX;
		this.defaultOffsetY = offsetY;
	}

	public void init() {
		graphics().rootLayer().add(rootGridLayer);
		drawGrid();
	}

	private void drawGrid() {
		float sizeWidth = getGridSquareWidth();
		float sizeHeight = getGridSquareHeight();
		int x = 0;
		int y = 0;
		for (float xOffset = defaultOffsetX; xOffset < screenWidth
				&& x < gridWidth; xOffset += sizeWidth) {
			for (float yOffset = defaultOffsetY; yOffset < screenHeight
					&& y < gridHeight; yOffset += sizeHeight) {
				LuxGridPosition gridPosition = grid.getGridPosition(x, y);

				HSBColor color = gridPosition.getPlayerHSBColor();
				if (!gridPosition.isNeutral()) {
					int f = gridPosition.getFortification();
					float hue = color.getHue();
					float saturation = color.getSaturation(); // ((0.35f * f) +
																// (1f * (99 -
																// f))) / 99;
					float brightness = ((1 * f) + (0.35f * (99 - f))) / 99;
					log("Color = H:" + hue + "  S:" + saturation + "  B:"
							+ brightness);
					color = new HSBColor(hue, saturation, brightness);
				}
				drawSquare(xOffset, yOffset, color.toRGB());

				if (debugOn) {
					if (!gridPosition.isNeutral()) {
						drawFortification(xOffset, yOffset,
								gridPosition.getFortification());
					}
				}

				ImageLayer canvasLayer = graphics().createImageLayer(cimage);
				rootGridLayer.add(canvasLayer);
				images[x][y] = canvasLayer;
				y++;
			}
			x++;
			y = 0;
		}
	}

	private void drawFortification(final float x, final float y,
			int fortification) {
		canvas.save();
		canvas.setFillColor(HSBColor.GRAY.toRGB());
		canvas.drawText(String.valueOf(fortification), x + getGridSquareWidth()
				/ 2, y + getGridSquareHeight() / 2);
		canvas.restore();
	}

	private void drawSquare(float originX, float originY, int fillColor) {
		canvas.save();
		canvas.setFillColor(fillColor);
		canvas.setStrokeColor(BLACK);
		canvas.strokeRect(originX, originY, getGridSquareWidth(),
				getGridSquareHeight());
		canvas.fillRect(originX + 0.5f, originY + 0.5f,
				getGridSquareWidth() - 0.5f, getGridSquareHeight() - 0.55f);
		canvas.restore();
	}

	private int getGridSquareHeight() {
		return (int) ((screenHeight - defaultOffsetY) / gridHeight);
	}

	private int getGridSquareWidth() {
		return (int) ((screenWidth - defaultOffsetX) / gridWidth);
	}

	/**
	 * Illuminates the given grid position.
	 * 
	 * @param gridX
	 *            - The x of the grid position
	 * @param gridY
	 *            - The y of the grid position
	 * @param fortification
	 *            - the grid position fortification
	 */
	public void illuminate(int gridX, int gridY, int fortification,
			LuxPlayer player) {
		// log("Illuminating at " + gridX + " " + gridY);
		ImageLayer imageLayer = images[gridX][gridY];
		if (null != imageLayer) {
			graphics().rootLayer().remove(imageLayer);
		} else {
			return;
		}

		logger.info("Fortification = " + fortification);
		int color = player.getColor();
		drawSquare(getScreenPositionX(gridX), getScreenPositionY(gridY), color);

		ImageLayer canvasLayer = graphics().createImageLayer(cimage);
		graphics().rootLayer().add(canvasLayer);
		images[gridX][gridY] = canvasLayer;
	}

	private void log(String string) {
		logger.info(string);
	}

	public float getScreenPositionX(int gridX) {
		return gridX * getGridSquareWidth();
	}

	public float getScreenPositionY(int gridY) {
		return gridY * getGridSquareHeight();
	}

	public void reset() {
		init();
	}

	public void paint(float alpha) {
		// clear images
		rootGridLayer.clear();
		images = new ImageLayer[gridWidth][gridHeight];
		// TODO: optimize not to draw the whole grid again
		drawGrid();
	}
}
