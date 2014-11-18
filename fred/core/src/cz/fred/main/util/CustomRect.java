package cz.fred.main.util;

import java.awt.Rectangle;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CustomRect extends Rectangle {

	private static final long serialVersionUID = 9206280504556055963L;
	
	public String name="";

	public CustomRect(String name, int parseInt, int parseInt2, int parseInt3, int parseInt4) {
		super(parseInt,parseInt2,parseInt3,parseInt4);
		this.name = name;
	}
	
	@Override
	public CustomRect clone() {
		CustomRect rect = new CustomRect(name, x, y, width, height);
		return rect;
	}

	public CustomRect() {
		super();
	}

	@Override
	public String toString() {
		return "name="+name+", x="+x+", y="+y+", width="+width+", height="+height;
	}
	
	@Override
	public boolean contains(int X, int Y) {
		int w = this.width;
		int h = this.height;
		if ((w | h) < 0) {
			// At least one of the dimensions is negative...
			return false;
		}
		// Note: if either dimension is zero, tests below must return false...
		int x = this.x;
		int y = this.y;
		if (X < x || Y < y) {
			return false;
		}
		w += x;
		h += y;
		//    overflow || intersect
		return ((w < x || w > X) &&
				(h < y || h > Y));
	}

	public Element getXML(Document doc) {
		Element box = doc.createElement("box");
		Attr attr = doc.createAttribute("name"); 
		attr.setValue(name);
		box.setAttributeNode(attr);
		attr = doc.createAttribute("x"); 
		attr.setValue(""+x);
		box.setAttributeNode(attr);
		attr = doc.createAttribute("y"); 
		attr.setValue(""+y);
		box.setAttributeNode(attr);
		attr = doc.createAttribute("width"); 
		attr.setValue(""+width);
		box.setAttributeNode(attr);
		attr = doc.createAttribute("height"); 
		attr.setValue(""+height);
		box.setAttributeNode(attr);
		
		return box;
	}
}
