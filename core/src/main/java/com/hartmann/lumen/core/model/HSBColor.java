package com.hartmann.lumen.core.model;

import java.awt.Color;

/**
 * Saves a color in the HSB space (Hue, Saturation and Brightness). <br>
 * Has a convenience method to convert it to the RGB color space.
 * 
 * @author Jonas Hartmann &lt;jonasharty@gmail.com&gt;
 * @since 30.11.2012
 * @see #toRGB()
 */
public final class HSBColor {

	public static final HSBColor BLUE = new HSBColor(0.65f, 1f, 1f);
	public static final HSBColor RED = new HSBColor(0f, 1f, 1f);
	public static final HSBColor GREEN = new HSBColor(0.35f, 1f, 1f);
	public static final HSBColor GRAY = new HSBColor(0f, 0.25f, 1f);

	private final float hue;
	private final float saturation;
	private final float brightness;

	/**
	 * The saturation and brightness components should be floating-point values
	 * between zero and one (numbers in the range 0.0-1.0). The hue component
	 * can be any floating-point number. The floor of this number is subtracted
	 * from it to create a fraction between 0 and 1. This fractional number is
	 * then multiplied by 360 to produce the hue angle in the HSB color model.
	 * 
	 * @param hue
	 * @param saturation
	 * @param brightness
	 */
	public HSBColor(float hue, float saturation, float brightness) {
		this.hue = hue;
		this.saturation = saturation;
		this.brightness = brightness;
	}

	/**
	 * Converts the components of a color, as specified by the HSB model, to an
	 * equivalent set of values for the default RGB model.
	 * <p>
	 * The integer that is returned encodes the value of a color in bits 0-23 of
	 * an integer value. This integer can be supplied as an argument to the
	 * {@link java.awt.Color} constructor that takes a single integer argument.
	 * </p>
	 * 
	 * @return An integer, which is the representation of the color in the RGB
	 *         color space.
	 */
	public int toRGB() {
		return Color.HSBtoRGB(getHue(), getSaturation(), getBrightness());
	}

	public float getHue() {
		return hue;
	}

	public float getSaturation() {
		return saturation;
	}

	public float getBrightness() {
		return brightness;
	}
}
