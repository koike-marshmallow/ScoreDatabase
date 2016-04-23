package jp.tkch.sdtool.manager;

import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import jp.tkch.sdtool.exporter.ScoreListContainerHtmlExporter;
import jp.tkch.sdtool.exporter.ScoreListHtmlExporter;
import jp.tkch.sdtool.gui.ScoreDataExportView;
import jp.tkch.sdtool.gui.ScoreDataExportViewListener;
import jp.tkch.sdtool.jindex.ScoreJindex;
import jp.tkch.sdtool.model.ScoreDataList;
import jp.tkch.sdtool.model.ScoreDataListContainer;

public class ScoreDatabaseExportManager
implements ScoreDataExportViewListener {
	public static String MSG_SUCCESS = "出力しました";
	public static String MSG_FAILED = "出力に失敗しました";

	private static String[] MENU = {"htmlファイル(一括)", "htmlファイル(個別)"};

	private ScoreDataList list;
	private ScoreDataExportView exportView;

	public ScoreDatabaseExportManager(ScoreDataList l0){
		list = l0;
		exportView = null;
	}

	public void showExportView(){
		exportView = new ScoreDataExportView(MENU);
		exportView.setListener(this);
		exportView.setVisible(true);
	}

	@Override
	public void inputCompleted(int code, int sType, File file) {
		if( code == ScoreDataExportView.APPROVE ){
			if( export(sType, file) ){
				JOptionPane.showMessageDialog(exportView, new JLabel(MSG_SUCCESS));
			}else{
				JOptionPane.showMessageDialog(exportView, new JLabel(MSG_FAILED));
			}
			exportView.dispose();
		}else if( code == ScoreDataExportView.CANCEL ){
			exportView.dispose();
		}else if( code == ScoreDataExportView.ERROR ){
			exportView.dispose();
		}
	}

	public boolean export(int type, File fp){
		switch( type ){
		case 0:
			return exportHtml(list, fp, false);
		case 1:
			return exportHtml(list, fp, true);
		}

		return false;
	}

	static boolean exportHtml(ScoreDataList dataList, File fp, boolean isIndex){
		ScoreListHtmlExporter exporter;

		if( isIndex ){
			exporter = new ScoreListContainerHtmlExporter(
				ScoreDataListContainer.createDividedScoreDataList
				(dataList, ScoreJindex.getScoreDivisor()));
		}else{
			exporter = new ScoreListHtmlExporter(dataList);
		}

		try {
			if( exporter.save(fp) ){
				return true;
			}else{
				System.err.println("export error");
				return false;
			}
		}catch(IOException e0){
			System.err.println("file error: " + e0.getMessage());
			return false;
		}
	}


}
