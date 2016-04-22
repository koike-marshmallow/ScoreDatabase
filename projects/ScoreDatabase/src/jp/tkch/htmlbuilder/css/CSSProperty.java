package jp.tkch.htmlbuilder.css;

public class CSSProperty {
	private String name;
	private String value;

	public CSSProperty(){
		name = "";
		value = "";
	}

	public CSSProperty(String n0, String v0){
		name = n0;
		value = v0;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public boolean isValid(){
		return (
			(name != null && !name.equals("")) &&
			(value != null && !value.equals(""))
		);
	}

	public String toString(){
		if( !isValid() ) return "";
		return name + ":" + value + ";";
	}
}
