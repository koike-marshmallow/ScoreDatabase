package jp.tkch.sdtool.model.comparator;

import java.util.Comparator;

import jp.tkch.sdtool.model.ScoreData;

public class ScoreDataIdComparator implements Comparator<ScoreData> {
	public int compare(ScoreData d1, ScoreData d2){
		return Integer.compare(d1.getId(), d2.getId());
	}
}
