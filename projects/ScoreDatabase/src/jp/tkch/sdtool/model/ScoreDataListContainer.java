package jp.tkch.sdtool.model;

import java.util.ArrayList;
import java.util.List;

public class ScoreDataListContainer {
	List<String> labels;
	List<ScoreDataList> lists;

	public ScoreDataListContainer(){
		labels = new ArrayList<String>();
		lists = new ArrayList<ScoreDataList>();
	}
	
	public void clear(){
		labels.clear();
		lists.clear();
	}
	
	public void add(String label, ScoreDataList list){
		labels.add(label);
		lists.add(list);
	}
	
	public void add(ScoreDataList list){
		add("", list);
	}
	
	public void add(int idx, String label, ScoreDataList list){
		labels.add(idx, label);
		lists.add(idx, list);
	}
	
	public void add(int idx, ScoreDataList list){
		add(idx, "", list);
	}
	
	public void setLabel(int idx, String label){
		labels.set(idx, label);
	}
	
	public void setList(int idx, ScoreDataList list){
		lists.set(idx, list);
	}
	
	public void set(int idx, String label, ScoreDataList list){
		setLabel(idx, label);
		setList(idx, list);
	}
	
	public void remove(int idx){
		labels.remove(idx);
		lists.remove(idx);
	}
	
	public String getLabel(int idx){
		return labels.get(idx);
	}
	
	public ScoreDataList getList(int idx){
		return lists.get(idx);
	}
	
	public int getListCount(){
		return lists.size();
	}
}
