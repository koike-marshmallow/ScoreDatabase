package jp.tkch.sdtool.model;

import java.util.ArrayList;
import java.util.List;

public class ScoreData {
	private int id;
	private String title;
	private String index;
	private List<ScoreDataParameter> params;

	public ScoreData(){
		setId(0);
		setTitle("NOTITLE");
		setIndex("NOTITLE");
		params = new ArrayList<ScoreDataParameter>();
	}

	public ScoreData(int i0){
		this();
		setId(i0);
	}

	public ScoreData(int i0, String t0){
		this();
		setId(i0);
		setTitle(t0);
	}

	public ScoreData(int i0, String t0, String x0){
		this();
		setId(i0);
		setTitle(t0);
		setIndex(x0);
	}

	public ScoreData(ScoreData sd0){
		this();
		setId(sd0.getId());
		setTitle(sd0.getTitle());
		setIndex(sd0.getIndex());
		for(ScoreDataParameter param : sd0.params){
			this.params.add(param);
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public void addParameter(String k0, String v0){
		params.add(new ScoreDataParameter(k0, v0));
	}

	public String getParameter(String k0){
		for(ScoreDataParameter param : params){
			if( param.getKey().equals(k0) ){
				return param.getValue();
			}
		}
		return null;
	}

	public String[] getParameters(String k0){
		List<String> result = new ArrayList<String>();

		for(ScoreDataParameter param : params){
			if( param.getKey().equals(k0) ){
				result.add(param.getValue());
			}
		}

		return result.toArray(new String[result.size()]);
	}

	public void clearParameters(String k0){
		for(ScoreDataParameter param : params){
			if( param.getKey().equals(k0) ){
				params.remove(param);
			}
		}
		return;
	}

	public void setParameter(String k0, String v0){
		for(ScoreDataParameter param : params){
			if( param.getKey().equals(k0) ){
				param.setValue(v0);
				return;
			}
		}
		params.add(new ScoreDataParameter(k0, v0));
	}

	public ScoreDataParameter[] getParameters(){
		return params.toArray(new ScoreDataParameter[params.size()]);
	}

}
