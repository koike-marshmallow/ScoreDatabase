package jp.tkch.sdtool.jindex;

import java.lang.Character.UnicodeBlock;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class JindexCtype {
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
