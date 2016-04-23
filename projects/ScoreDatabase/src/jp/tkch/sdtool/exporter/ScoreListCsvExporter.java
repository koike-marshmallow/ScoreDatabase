package jp.tkch.sdtool.exporter;

import jp.tkch.sdtool.model.ScoreData;
import jp.tkch.sdtool.model.ScoreDataList;

public class ScoreListCsvExporter {
	public static String[] toRow(ScoreData data){
		String[] column = new String[6];
		column[0] = String.valueOf(data.getId());
		column[1] = data.getTitle();
		column[2] = data.getIndex();
		column[3] = data.getParameter("author");
		column[4] = data.getParameter("publisher");
		column[5] = data.getParameter("comment");

		return column;
	}

	public static ScoreData parseRow(String[] column){
		ScoreData data;
		if( column.length > 0 ){
			try {
				data = new ScoreData(Integer.parseInt(column[0]));
			}catch(NumberFormatException e0){
				return null;
			}
		}else{
			return null;
		}

		if( column.length > 1 ) data.setTitle(column[1]);
		if( column.length > 2 ) data.setIndex(column[2]);
		if( column.length > 3 ) data.setParameter("author", column[3]);
		if( column.length > 4 ) data.setParameter("publisher", column[4]);
		if( column.length > 5 ) data.setParameter("comment", column[5]);

		return data;
	}


	private ScoreDataList list;

	public ScoreListCsvExporter(){
		list = null;
	}

	public ScoreListCsvExporter(ScoreDataList l0){
		list = l0;
	}



}
