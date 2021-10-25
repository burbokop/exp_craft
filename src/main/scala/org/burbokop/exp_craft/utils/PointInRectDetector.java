package org.burbokop.exp_craft.utils;

import javax.vecmath.Point2i;

import org.lwjgl.util.Rectangle;

public class PointInRectDetector {
	public static boolean detect(Point2i point, Point2i rectOffset, Rectangle rect) {
		return 
				point.getX() >= (rect.getX() + rectOffset.getX()) && 
				point.getX() < (rect.getX() + rectOffset.getX() + rect.getWidth()) &&
				point.getY() >= (rect.getY() + rectOffset.getY()) && 
				point.getY() < (rect.getY() + rectOffset.getY() + rect.getHeight());
	}
}
