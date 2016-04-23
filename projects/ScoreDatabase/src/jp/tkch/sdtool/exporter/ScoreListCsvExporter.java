package jp.tkch.sdtool.exporter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.sf.orangesignal.csv.Csv;
import jp.sf.orangesignal.csv.CsvConfig;
import jp.sf.orangesignal.csv.handlers.StringArrayListHandler;
import jp.tkch.sdtool.model.ScoreData;
import jp.tkch.sdtool.model.ScoreDataList;

public class ScoreListCsvExporter {
	public static String[] CONTENT_HEADER =
		{"#id", "title", "index", "author", "publisher", "comment"};

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
	private List<String[]> csvData;
	private List<String> errorMsg;

	public ScoreListCsvExporter(){
		list = null;
		errorMsg = new ArrayList<String>();
	}

	public ScoreListCsvExporter(ScoreDataList l0){
		list = l0;
	}

	public void setScoreDataList(ScoreDataList l0){
		list = l0;
	}

	public ScoreDataList getScoreDataList(){
		return list;
	}

	public int getErrorCount(){
		return errorMsg.size();
	}

	public String[] getErrorMessages(){
		return errorMsg.toArray(new String[errorMsg.size()]);
	}

	boolean build(){
		csvData = new ArrayList<String[]>();

		csvData.add(CONTENT_HEADER);

		if( list != null ){
			for(int i=0; i<list.getDataCount(); i++){
				csvData.add(toRow(list.get(i)));
			}
		}else{
			errorMsg.add("ScoreDataList is null");
		}

		return true;
	}

	boolean parse(){
		list = new ScoreDataList();

		if( csvData != null ){
			for(int i=0; i<csvData.size(); i++){
				String[] row = csvData.get(i);
				if( row.length > 0 && row[0].charAt(0) != '#' ){
					ScoreData data = parseRow(row);
					if( data != null ){
						list.add(data);
					}else{
						errorMsg.add("[LINE:" + (i+1) + "] parse error");
					}
				}
			}
		}else{
			errorMsg.add("CsvData is null");
		}

		return true;
	}

	public boolean save(File fp)
	throws IOException{
		if( !build() ) return false;

		Csv.save(csvData, fp, new CsvConfig(), new StringArrayListHandler());

		return true;
	}

	public boolean load(File fp)
	throws IOException{
		csvData = Csv.load(fp, new CsvConfig(), new StringArrayListHandler());

		if( !parse() ) return false;

		return true;
	}
}
