package jp.tkch.sdtool.jindex;

import java.lang.Character.UnicodeBlock;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class JindexCtype {
	public static final int CTYPE_NUM = 101;
	public static final int CTYPE_ALPH = 102;
	public static final int CTYPE_HIRAGANA = 103;
	public static final int CTYPE_KANAKANA = 104;
	public static final int CTYPE_KANJI = 105;
	public static final int CTYPE_OTHERS = 106;
	
	public static int of(char c){
		int block = UnicodeBlock.of(c);
		
		switch( block ){
		case UnicodeBlock.HIRAGANA:
			return CTYPE_HIRAGANA;
		case UnicodeBlock.KATAKANA:
			return CTYPE_KATAKANA;
		case UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS:
			return CTYPE_KANJI;
		case UnicodeBlock.BASIC_LATIN:
			if( c >= '0' && c <= '9' ){
				return CTYPE_NUM;
			}else if( 
				(c >= 'A' && c <= 'Z') ||
				(c >= 'a' && c <= 'z')
			){
				return CTYPE_ALPH;
			}else{
				return CTYPE_OTHERS;
			}
		default:
			return CTYPE_OTHERS;
		}
		
		return 0;
	}


	public static void main(String[] args){
		String str = "あぁアァ今a1@Ａ";
		List<Character> cs = new ArrayList<Character>();

		for(int i=0; i<str.length(); i++){
			char c = str.charAt(i);
			System.out.print("[" + i + "] \'" + c + "\': ");
			System.out.println(UnicodeBlock.of(c).toString());
			cs.add(c);
		}


		Collections.sort(cs, new Comparator<Character>(){
			Collator col = Collator.getInstance(Locale.JAPANESE);
			public int compare(Character c1, Character c2){
				String s1 = "" + c1;
				String s2 = "" + c2;
				return col.compare(s1, s2);
			}
		});
		
		for(Character c : cs){
			System.out.print(c + " ");
			
		}
	}
}
