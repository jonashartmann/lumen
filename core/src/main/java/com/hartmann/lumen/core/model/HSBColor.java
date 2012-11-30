package com.hartmann.lumen.core.model;

import java.awt.Color;

public final class HSBColor {

	public static final HSBColor BLUE = new HSBColor(0.65f, 1f, 1f);
	public static final HSBColor RED = new HSBColor(0f, 1f, 1f);
	public static final HSBColor GREEN = new HSBColor(0.35f, 1f, 1f);
	public static final HSBColor GRAY = new HSBColor(0f, 0.25f, 1f);

	private final float hue;
	private final float saturation;
	private final float brightness;

	public HSBColor(float hue, float saturation, float brightness) {
		this.hue = hue;
		this.saturation = saturation;
		this.brightness = brightness;
	}

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
