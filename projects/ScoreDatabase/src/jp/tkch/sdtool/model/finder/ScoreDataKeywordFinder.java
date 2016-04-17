package jp.tkch.sdtool.model.finder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jp.tkch.sdtool.model.ScoreData;
import jp.tkch.sdtool.model.ScoreDataFinder;

public class ScoreDataKeywordFinder
implements ScoreDataFinder {
	public static final int TARGET_TITLE = 101;
	public static final int TARGET_AUTHOR = 102;
	public static final int TARGET_PUBLISHER = 103;
	public static final int TARGET_COMMENT = 104;

	public static final int LOGIC_AND = 201;
	public static final int LOGIC_OR = 202;

	private boolean isTargetTitle, isTargetAuthor;
	private boolean isTargetPublisher, isTargetComment;

	private boolean isLogicAnd;

	private List<String> keywords;


	public ScoreDataKeywordFinder(){
		isTargetTitle = true;
		isTargetAuthor = true;
		isTargetPublisher = false;
		isTargetComment = false;
		isLogicAnd = false;
		keywords = new ArrayList<String>();
	}

	public ScoreDataKeywordFinder(String keywords){
		this();
		addKeywords(keywords);
	}

	public void addKeywords(String str){
		Scanner scanner = new Scanner(str);
		while( scanner.hasNext() ){
			keywords.add(scanner.next());
		}
		scanner.close();
	}

	public void addKeywords(String[] strs){
		for(String str : strs){
			keywords.add(str);
		}
	}

	public void clearKeywords(){
		keywords.clear();
	}

	public void setTarget(int tcode, boolean b0){
		switch( tcode ){
		case TARGET_TITLE:
			isTargetTitle = b0;
			break;
		case TARGET_AUTHOR:
			isTargetAuthor = b0;
			break;
		case TARGET_PUBLISHER:
			isTargetPublisher = b0;
			break;
		case TARGET_COMMENT:
			isTargetComment = b0;
			break;
		}
	}

	public void setLogic(int lcode){
		if( lcode == LOGIC_AND ){
			isLogicAnd = true;
		}else if( lcode == LOGIC_OR ){
			isLogicAnd = false;
		}
	}

	public boolean isStringMatch(String str, boolean isAnd){
		boolean tmp;
		for(String keyword : keywords){
			tmp = (str.indexOf(keyword) != -1);
			if( tmp && !isAnd ) return true;
			if( !tmp && isAnd ) return false;
		}
		return isAnd;
	}

	@Override
	public boolean isMatch(ScoreData data) {
		if( isTargetTitle ){
			if( isStringMatch(data.getTitle(), isLogicAnd) ){
				return true;
			}
		}
		if( isTargetAuthor ){
			if( isStringMatch(data.getParameter("author"), isLogicAnd) ){
				return true;
			}
		}
		if( isTargetPublisher ){
			if( isStringMatch(data.getParameter("publisher"), isLogicAnd) ){
				return true;
			}
		}
		if( isTargetComment ){
			if( isStringMatch(data.getParameter("comment"), isLogicAnd) ){
				return true;
			}
		}
		return false;
	}
}
