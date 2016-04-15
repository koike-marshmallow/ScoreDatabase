package jp.tkch.sdtool.model;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ScoreDataList {
	private List<ScoreData> scores;
	private Comparator<Object> comparator;

	public ScoreDataList(){
		scores = new ArrayList<ScoreData>();
		comparator = Collator.getInstance(Locale.JAPANESE);
	}

	public ScoreDataList(Comparator<Object> cmp0){
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

		idx = 0;
		while(
			idx < scores.size() &&
			(comparator.compare(scores.get(idx).getIndex(), insert.getIndex()) <= 0)
		){
			idx++;
		}

		scores.add(idx, insert);
	}

	public void remove(int id){
		for(int i=0; i<scores.size(); i++){
			if( scores.get(i).getId() == id ){
				scores.remove(i);
				return;
			}
		}
	}

	public int getMaxId(){
		int max = 0;
		for(int i=0; i<scores.size(); i++){
			if( max < scores.get(i).getId() ){
				max = scores.get(i).getId();
			}
		}
		return max;
	}

	public ScoreDataList createList(Comparator<ScoreData> compr){
		ScoreDataList nlist = new ScoreDataList(comparator);
		for(ScoreData sdata : scores){
			if( compr.equals(sdata) ){
				nlist.add(sdata);
			}
		}
		return nlist;
	}

	public ScoreData[] toArray(){
		return scores.toArray(new ScoreData[scores.size()]);
	}
}