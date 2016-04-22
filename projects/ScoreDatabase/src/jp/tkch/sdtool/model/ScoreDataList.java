package jp.tkch.sdtool.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jp.tkch.sdtool.model.comparator.ScoreDataIdComparator;

public class ScoreDataList {

	private List<ScoreData> scores;
	private Comparator<ScoreData> comparator;

	public ScoreDataList(){
		scores = new ArrayList<ScoreData>();
		comparator = null;
	}

	public ScoreDataList(Comparator<ScoreData> cmp0){
		scores = new ArrayList<ScoreData>();
		comparator = cmp0;
	}

	public boolean isIdRegisted(int id){
		for(int i=0; i<scores.size(); i++){
			if( scores.get(i).getId() == id ){
				return true;
			}
		}
		return false;
	}

	public void add(ScoreData data){
		int idx;
		ScoreData insert = new ScoreData(data);

		if( isIdRegisted(insert.getId()) ){
			remove(insert.getId());
		}

		if( comparator != null ){
			idx = 0;
			while(
				idx < scores.size() &&
				(comparator.compare(scores.get(idx), insert) <= 0)
			){
				idx++;
			}
		}else{
			idx = scores.size();
		}

		scores.add(idx, insert);
	}

	public void add(ScoreDataList list){
		for(int i=0; i<list.getDataCount(); i++){
			add(list.get(i));
		}
	}

	public void remove(int id){
		for(int i=0; i<scores.size(); i++){
			if( scores.get(i).getId() == id ){
				scores.remove(i);
				return;
			}
		}
	}

	public ScoreData get(int idx){
		if( idx >= 0 && idx < scores.size() ){
			return new ScoreData(scores.get(idx));
		}
		return null;
	}

	public ScoreData getById(int id){
		for(ScoreData data : scores){
			if( data.getId() == id ){
				return new ScoreData(data);
			}
		}
		return null;
	}

	public int getMaxId(){
		if( scores.size() <= 0 ) return -1;
		return Collections.max(scores, new ScoreDataIdComparator()).getId();
	}

	public void setComparator(Comparator<ScoreData> c0){
		comparator = c0;
		Collections.sort(scores, c0);
	}

	public ScoreDataList createDataList(ScoreDataFinder finder){
		ScoreDataList result = new ScoreDataList();
		for(ScoreData sd : scores){
			if( finder.isMatch(sd) ){
				result.add(sd);
			}
		}
		return result;
	}

	public int getDataCount(){
		return scores.size();
	}

	public ScoreData[] toArray(){
		return scores.toArray(new ScoreData[scores.size()]);
	}
}