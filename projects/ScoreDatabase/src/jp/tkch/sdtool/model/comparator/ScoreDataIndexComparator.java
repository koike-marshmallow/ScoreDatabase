package jp.tkch.sdtool.model.comparator;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import jp.tkch.sdtool.model.ScoreData;

public class ScoreDataIndexComparator implements Comparator<ScoreData> {
	private Comparator<Object> comparator;

	public ScoreDataIndexComparator(){
		comparator = Collator.getInstance(Locale.JAPANESE);
	}

	public ScoreDataIndexComparator(Comparator<Object> comp){
		comparator = comp;
	}

	public int compare(ScoreData d1, ScoreData d2){
		return comparator.compare(d1.getIndex(), d2.getIndex());
	}
}
