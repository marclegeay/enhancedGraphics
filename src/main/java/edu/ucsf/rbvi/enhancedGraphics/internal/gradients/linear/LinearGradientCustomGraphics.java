package edu.ucsf.rbvi.enhancedGraphics.internal.gradients.linear;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cytoscape.model.CyNode;
import org.cytoscape.view.presentation.customgraphics.CyCustomGraphics;
import org.cytoscape.view.presentation.customgraphics.CyCustomGraphicsFactory;
import org.cytoscape.view.presentation.customgraphics.CustomGraphicLayer;

import edu.ucsf.rbvi.enhancedGraphics.internal.AbstractEnhancedCustomGraphics;

public class LinearGradientCustomGraphics extends AbstractEnhancedCustomGraphics<LinearGradientLayer> {

	// Parse the input string, which is always of the form:
	// 	lingrad: start="x,y" end="x,y" stoplist="r,g,b,a,stop|r,g,b,a,stop|r,g,b,a,stop"
	public LinearGradientCustomGraphics(String input) {
		Map<String, String> inputMap = parseInput(input);
		Point2D start = new Point2D.Float(0.0f, 0.0f);
		Point2D end = new Point2D.Float(1.0f, 0.0f);

		// Create our defaults
		List<Float> stopList = new ArrayList<Float>();
		List<Color> colorList = new ArrayList<Color>();
		int nStops = 0;

		if (inputMap.containsKey("start")) {
			start = parsePoint(inputMap.get("start"));
			if (start == null) {
				logger.error("Not able to parse start point from '"+inputMap.get("start")+"'");
				return;
			}
		}
		if (inputMap.containsKey("end")) {
			end = parsePoint(inputMap.get("end"));
			if (end == null) {
				logger.error("Not able to parse end point from '"+inputMap.get("end")+"'");
				return;
			}
		}
		if (inputMap.containsKey("stoplist")) {
			nStops = parseStopList(inputMap.get("stoplist"), colorList, stopList);
			if (nStops == 0) {
				logger.error("Not able to stop list from '"+inputMap.get("stoplist")+"'");
				return;
			}
		}
		LinearGradientLayer cg = new LinearGradientLayer(colorList, stopList, start, end);
		layers.add(cg);
	}

	public String toSerializableString() { return this.getIdentifier().toString()+","+displayName; }

	public Image getRenderedImage() {
		CustomGraphicLayer cg = layers.get(0);
		// Create a rectangle and fill it with our current paint
		Rectangle rect = cg.getBounds2D().getBounds();
		BufferedImage image = new BufferedImage(rect.width, rect.height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setPaint(cg.getPaint(rect));
		g2d.fill(rect);
		return image;
	}
}
