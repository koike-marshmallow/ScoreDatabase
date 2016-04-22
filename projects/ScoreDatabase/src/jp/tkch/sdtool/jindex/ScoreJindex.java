package jp.tkch.sdtool.jindex;

import jp.tkch.sdtool.model.ScoreData;
import jp.tkch.sdtool.model.ScoreDivisor;

public class ScoreJindex{
	static String[][] BOUNDS = {
			{"09", "az", "AZ"},
			{"アオ"}, {"カゴ"}, {"サゾ"},
			{"タド"}, {"ナノ"}, {"ハポ"},
			{"マモ"}, {"ャヨ"}, {"ラロ"},
			{"ヮン"}
	};
	public static final int CTYPE_UNDEFINED = -1;
	public static final int CTYPE_OTHERS = 11;

	static String[] LABELS = {
		"英数字", "あ行", "か行", "さ行", "た行", "な行",
		"は行", "ま行", "や行", "ら行", "わ行", "その他"
	};

	public static int getType(String str){
		if( str != null && str.length() > 0 ){
			return getType(str.charAt(0));
		}else{
			return CTYPE_OTHERS;
		}
	}

	public static int getType(char c){
		for(int i=0; i<BOUNDS.length; i++){
			if( isBound(c, BOUNDS[i]) ){
				return i;
			}
		}
		return CTYPE_OTHERS;
	}

	static boolean isBound(char c, String[] start_end){
		for(String se : start_end ){
			if( se.length() >= 2 ){
				if( c >= se.charAt(0) && c <= se.charAt(1) ){
					return true;
				}
			}
		}
		return false;
	}

	public static String getLabel(int idx){
		if( idx >= 0 && idx <= LABELS.length ){
			return LABELS[idx];
		}
		return "undefined";
	}

	public static ScoreDivisor getScoreDivisor(){
		return new ScoreDivisor(){

			@Override
			public boolean isValid(ScoreData data) {
				return (data != null && data.getIndex() != null);
			}

			@Override
			public int groupNumberOf(ScoreData data) {
				if( isValid(data) ){
					return getType(data.getIndex());
				}else{
					return GROUP_UNDEFINED;
				}
			}

			@Override
			public int getGroupCount() {
				return LABELS.length;
			}

			@Override
			public String getGroupLabel(int num) {
				return getLabel(num);
			}

		};
	}
}
