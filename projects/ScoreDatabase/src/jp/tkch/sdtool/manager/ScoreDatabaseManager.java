package jp.tkch.sdtool.manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Comparator;

import javax.swing.JButton;

import jp.tkch.sdtool.gui.ScoreDataListTableModel;
import jp.tkch.sdtool.gui.ScoreTableView;
import jp.tkch.sdtool.model.ScoreData;
import jp.tkch.sdtool.model.comparator.ScoreDataIdComparator;

public class ScoreDatabaseManager{
	static Comparator<ScoreData> DEFAULT_COMPARATOR = new ScoreDataIdComparator();
	static File DEFAULT_DB_FILE = new File("db.xml");

	private DualScoreDataList list;
	private ScoreTableView tableView;
	private JButton btnAdd, btnEdit, btnDelete;
	private JButton btnSearch, btnExport, btnImport;


	public void init(){
		list = new DualScoreDataList(DEFAULT_COMPARATOR);
		list.setDbFile(DEFAULT_DB_FILE);
		list.loadDatabase();
		list.resetCurrent();

		buildTableView();
	}

	void buildTableView(){
		//インスタンス初期化
		tableView = new ScoreTableView();

		//コンポーネントの追加
		btnAdd = new JButton("追加");
		btnAdd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				addButtonPressed();
			}
		});
		tableView.addControlComponent(btnAdd);
		btnEdit = new JButton("編集");
		btnEdit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				editButtonPressed();
			}
		});
		tableView.addControlComponent(btnEdit);
		btnDelete = new JButton("削除");
		btnDelete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				deleteButtonPressed();
			}
		});
		tableView.addControlComponent(btnDelete);
		btnSearch = new JButton("検索");
		btnSearch.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				searchButtonPressed();
			}
		});
		tableView.addControlComponent(btnSearch);
		btnExport = new JButton("ファイル出力");
		btnExport.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				exportButtonPressed();
			}
		});
		tableView.addControlComponent(btnExport);
		btnImport = new JButton("csvからインポート");
		btnImport.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e0){
				importButtonPressed();
			}
		});
		tableView.addControlComponent(btnImport);

		//テーブルを設定
		tableView.setScoreDataListTableModel(
			new ScoreDataListTableModel(list.getCurrent()));
	}

	public void excute(){
		tableView.setVisible(true);
	}

	void addButtonPressed(){
		ScoreDatabaseEditManager manager =
			new ScoreDatabaseEditManager(tableView, list.getMaster());
		manager.setListener(new ReloadListener(){
			public void reload(){
				commitTable(true);
			}
		});

		manager.excuteAddMode();
	}

	void editButtonPressed(){
		if( tableView.getSelectedRowCount() > 0 ){
			ScoreDatabaseEditManager manager =
				new ScoreDatabaseEditManager(tableView, list.getMaster());
			manager.setListener(new ReloadListener(){
				public void reload(){
					commitTable(true);
				}
			});

			for(int row : tableView.getSelectedRows()){
				manager.enqueue(list.getCurrent().get(row).getId());
			}
			manager.excuteEditMode();
		}else{
			tableView.alert("編集するデータを選択してください");
		}
	}

	void deleteButtonPressed(){
		if( tableView.getSelectedRowCount() > 0 ){
			ScoreDatabaseEditManager manager =
				new ScoreDatabaseEditManager(tableView, list.getMaster());
			manager.setListener(new ReloadListener(){
				public void reload(){
					commitTable(true);
				}
			});

			for(int row : tableView.getSelectedRows()){
				manager.enqueue(list.getCurrent().get(row).getId());
			}
			manager.excuteDeleteMode();
		}else{
			tableView.alert("削除するデータを選択してください");
		}
	}

	void searchButtonPressed(){
		ScoreDatabaseSearchManager manager =
			new ScoreDatabaseSearchManager(list);
		manager.setListener(new ReloadListener(){
			public void reload(){
				commitTable(false);
			}
		});

		manager.excute();
	}

	void exportButtonPressed(){
		ScoreDatabaseExportManager manager =
			new ScoreDatabaseExportManager(list.getCurrent());
		manager.excute();
	}

	void importButtonPressed(){
		ScoreDatabaseCsvImportManager manager =
			new ScoreDatabaseCsvImportManager(list);
		manager.setListener(new ReloadListener(){
			public void reload(){
				commitTable(true);
			}
		});
		manager.excute();
	}

	void commitTable(boolean currentSync){
		list.saveDatabase();
		if( currentSync ){
			list.resetCurrent();
		}
		tableView.setScoreDataListTableModel(
			new ScoreDataListTableModel(list.getCurrent()));
	}


	public static void main(String[] args){
		ScoreDatabaseManager manager = new ScoreDatabaseManager();
		manager.init();
		manager.excute();
	}
}
