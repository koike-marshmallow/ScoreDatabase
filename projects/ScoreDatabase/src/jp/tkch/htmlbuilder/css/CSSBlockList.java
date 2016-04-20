package jp.tkch.htmlbuilder.css;

import java.util.ArrayList;
import java.util.List;

public class CSSBlockList {
	List<CSSBlock> list;

	public CSSBlockList(){
		list = new ArrayList<CSSBlock>();
	}

	public void add(CSSBlock b0){
		list.add(b0);
	}

	public CSSBlock get(int idx){
		return list.get(idx);
	}

	public void clear(){
		list.clear();
	}

	public int getLength(){
		return list.size();
	}

	public String toString(){
		String merge = "";
		for(CSSBlock block : list){
			merge += block.toString() + "\n";
		}

		return merge;
	}
}
