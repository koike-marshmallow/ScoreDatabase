package jp.tkch.sdtool.manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import jp.tkch.sdtool.exporter.ScoreListCsvExporter;
import jp.tkch.sdtool.gui.ScoreDataListTableModel;
import jp.tkch.sdtool.gui.ScoreTableView;
import jp.tkch.sdtool.model.ScoreData;
import jp.tkch.sdtool.model.ScoreDataList;

public class ScoreDatabaseCsvImportManager {
	private DualScoreDataList list;
	private ScoreTableView tableView;
	private ScoreDataList tmpList;
	private JButton btnLoad, btnRemove, btnExcute;
	private JRadioButton rbOver, rbSkip, rbRefresh;
	private int recentErrorCount;
	private ReloadListener listener;

	public ScoreDatabaseCsvImportManager(DualScoreDataList l0){
		list = l0;
		tableView = null;
		tmpList = null;
		recentErrorCount = 0;
		listener = null;
	}

	public void setListener(ReloadListener l0){
		listener = l0;
	}

	void createTableView(){
		tableView = new ScoreTableView();
		tableView.setDefaultCloseOperation(ScoreTableView.DISPOSE_ON_CLOSE);

		btnLoad = new JButton("新規読み込み");
		btnLoad.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e0){
				loadButtonPressed();
			}
		});
		tableView.addControlComponent(btnLoad);
		btnRemove = new JButton("選択したデータを除去");
		btnRemove.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e0){
				removeButtonPressed();
			}
		});
		tableView.addControlComponent(btnRemove);
		btnExcute = new JButton("確定");
		btnExcute.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e0){
				excuteButtonPressed();
			}
		});
		tableView.addControlComponent(btnExcute);

		ButtonGroup bGroup = new ButtonGroup();
		rbOver = new JRadioButton("上書き", true);
		bGroup.add(rbOver);
		tableView.addControlComponent(rbOver);
		rbSkip = new JRadioButton("スキップ");
		bGroup.add(rbSkip);
		tableView.addControlComponent(rbSkip);
		rbRefresh = new JRadioButton("初期化");
		bGroup.add(rbRefresh);
		tableView.addControlComponent(rbRefresh);
	}

	void showTableView(){
		createTableView();
		tableView.setVisible(true);
	}

	boolean loadCsvFile(File fp){
		ScoreListCsvExporter exporter = new ScoreListCsvExporter();

		try {
			if( exporter.load(fp) ){
				recentErrorCount = exporter.getErrorCount();
				tmpList = exporter.getScoreDataList();
				return true;
			}else{
				tableView.alert("読み込みエラーが発生しました");
				return false;
			}
		}catch(IOException e0){
			tableView.alert("ファイルエラー: " + e0.getMessage());
			return false;
		}
	}

	void loadButtonPressed(){
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(
			new FileNameExtensionFilter("CSVファイル", "csv"));

		int sel = chooser.showOpenDialog(tableView);
		if( sel == JFileChooser.APPROVE_OPTION ){
			if( loadCsvFile(chooser.getSelectedFile()) ){
				tableView.setScoreDataListTableModel(
					new ScoreDataListTableModel(tmpList));
				if( recentErrorCount > 0 ){
					tableView.setStatusMessage("項目数: " + tmpList.getDataCount() +
							"(" + recentErrorCount + "個の警告があります)");
				}
			}
		}

		if( sel == JFileChooser.ERROR_OPTION ){
			tableView.alert("エラーが発生しました");
		}
	}

	void removeButtonPressed(){
		int count = tableView.getSelectedRowCount();

		if( count > 0 ){
			ScoreDatabaseEditManager manager =
				new ScoreDatabaseEditManager(tableView, tmpList);
			manager.setListener(new ReloadListener(){
				public void reload(){
					tableView.setScoreDataListTableModel(
						new ScoreDataListTableModel(tmpList));
				}
			});
			for(int row : tableView.getSelectedRows()) manager.enqueue(tmpList.get(row).getId());
			manager.excuteDeleteMode();
		}else{
			tableView.alert("除去するデータを選択して下さい");
		}
	}

	void excuteButtonPressed(){
		if( tmpList != null ){
			if( rbRefresh.isSelected() ){
				list.getMaster().clear();
			}

			for(int i=0; i<tmpList.getDataCount(); i++){
				ScoreData data = tmpList.get(i);
				if( list.getMaster().isIdRegisted(data.getId()) ){
					if( rbOver.isSelected() ){
						list.getMaster().add(data);
					}
				}else{
					list.getMaster().add(data);
				}
			}
		}

		tableView.alert("データを登録しました");
		tableView.dispose();
		if( listener != null ) listener.reload();
	}

	public void excute(){
		showTableView();
	}
}
