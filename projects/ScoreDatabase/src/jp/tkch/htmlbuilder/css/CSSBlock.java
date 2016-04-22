package jp.tkch.htmlbuilder.css;

import java.util.ArrayList;
import java.util.List;

public class CSSBlock {
	private String selector;
	private List<CSSProperty> properties;

	public CSSBlock(){
		selector = "";
		properties = new ArrayList<CSSProperty>();
	}

	public CSSBlock(String s0){
		this();
		selector = s0;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public CSSBlock addProperty(CSSProperty p0){
		properties.add(p0);
		return this;
	}

	public void clearProperty(){
		properties.clear();
	}

	public boolean isValid(){
		return (selector != null && !selector.equals(""));
	}

	public String toString(){
		String str = "";
		if( isValid() ){
			str = selector + "{";
			for(CSSProperty property : properties){
				str += property.toString();
			}
			str += "}";
		}
		return str;
	}
}
