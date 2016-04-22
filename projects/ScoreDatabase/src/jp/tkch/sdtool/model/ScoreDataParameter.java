package jp.tkch.sdtool.model;

public class ScoreDataParameter {
	private String key;
	private String value;

	public ScoreDataParameter(){
		setKey("");
		setValue("");
	}

	public ScoreDataParameter(String k0, String v0){
		setKey(k0);
		setValue(v0);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


}
