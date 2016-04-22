package jp.tkch.sdtool.manager;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import jp.tkch.sdtool.exporter.ScoreListContainerHtmlExporter;
import jp.tkch.sdtool.exporter.ScoreListXmlExporter;
import jp.tkch.sdtool.gui.ScoreDataEditorView;
import jp.tkch.sdtool.gui.ScoreDataEditorViewListener;
import jp.tkch.sdtool.gui.ScoreDataListTableModel;
import jp.tkch.sdtool.gui.ScoreDataSearchView;
import jp.tkch.sdtool.gui.ScoreDataSearchViewListener;
import jp.tkch.sdtool.gui.ScoreTableView;
import jp.tkch.sdtool.jindex.ScoreJindex;
import jp.tkch.sdtool.model.ScoreData;
import jp.tkch.sdtool.model.ScoreDataFinder;
import jp.tkch.sdtool.model.ScoreDataList;
import jp.tkch.sdtool.model.ScoreDataListContainer;
import jp.tkch.sdtool.model.comparator.ScoreDataIdComparator;

public class ScoreDatabaseManager
implements ActionListener, ScoreDataEditorViewListener,
ScoreDataSearchViewListener{
	public static final int MODE_ADD = 1001;
	public static final int MODE_EDIT = 1002;

	public static Comparator<ScoreData> DEFAULT_COMPARATOR = new ScoreDataIdComparator();

	private ScoreDataList master;
	private ScoreDataList current;
	private ScoreTableView tableView;
	private ScoreDataEditorView editorView;
	private ScoreDataSearchView searchView;
	private JButton ctrlAdd, ctrlSearch, ctrlEdit, ctrlDelete, ctrlExport;
	private Queue<Integer> editQueue;
	private int editMode;

	public ScoreDatabaseManager(){
		master = new ScoreDataList(DEFAULT_COMPARATOR);
		editQueue = new ArrayDeque<Integer>();
		current = master;
		init();
	}

	public void init(){
		tableView = new ScoreTableView();
		ctrlAdd = new JButton("追加");
		ctrlAdd.addActionListener(this);
		ctrlSearch = new JButton("検索");
		ctrlSearch.addActionListener(this);
		ctrlEdit = new JButton("編集");
		ctrlEdit.addActionListener(this);
		ctrlDelete = new JButton("削除");
		ctrlDelete.addActionListener(this);
		ctrlExport = new JButton("出力");
		ctrlExport.addActionListener(this);
		tableView.addControlComponent(ctrlAdd);
		tableView.addControlComponent(ctrlSearch);
		tableView.addControlComponent(ctrlEdit);
		tableView.addControlComponent(ctrlDelete);
		tableView.addControlComponent(ctrlExport);
		loadDatabase();
		current = master;
		reloadTable();
		tableView.setVisible(true);
	}

	public void reloadTable(){
		tableView.setScoreDataListTableModel(new ScoreDataListTableModel(current));
	}

	public void saveDatabase(){
		ScoreListXmlExporter exporter = new ScoreListXmlExporter();
		exporter.setScoreDataList(master);
		try{
			if( !exporter.save(new File("db.xml")) ){
				System.out.println("データ出力中にエラーが発生しました");
			}
		}catch(IOException e0){
			System.out.println(e0.getMessage());
		}
	}

	public void loadDatabase(){
		ScoreListXmlExporter exporter = new ScoreListXmlExporter();
		try{
			if( !exporter.load(new File("db.xml")) ){
				System.out.println("データ読込中にエラーが発生しました");
			}
		}catch(IOException e0){
			System.out.println(e0.getMessage());
		}
		if( exporter.getScoreDataList() != null ){
			master = exporter.getScoreDataList();
			master.setComparator(DEFAULT_COMPARATOR);
		}
	}


	public boolean addScoreData(ScoreData sdata, Component parent){
		if( master.isIdRegisted(sdata.getId()) ){
			if( parent != null ){
				JOptionPane.showMessageDialog(parent, new JLabel("idがすでに登録されています"));
			}
			return false;
		}
		if( !isIndexValid(sdata.getIndex()) ){
			int sel = JOptionPane.YES_OPTION;
			if( parent != null ){
				sel = JOptionPane.showConfirmDialog(
					parent, new JLabel("よみがなに無効な文字が含まれています\nこのまま登録しますか？"));
			}
			if( sel != JOptionPane.YES_OPTION ){
				return false;
			}
		}

		master.add(sdata);
		saveDatabase();
		reloadTable();
		return true;
	}


	public boolean setScoreData(ScoreData sdata, Component parent){
		if( !master.isIdRegisted(sdata.getId()) ){
			if( parent != null ){
				JOptionPane.showMessageDialog(parent, new JLabel("idが見つかりません"));
			}
			return false;
		}
		if( !isIndexValid(sdata.getIndex()) ){
			int sel = JOptionPane.YES_OPTION;
			if( parent != null ){
				sel = JOptionPane.showConfirmDialog(
					parent, new JLabel("よみがなに無効な文字が含まれています\nこのまま登録しますか？"));
			}
			if( sel != JOptionPane.YES_OPTION ){
				return false;
			}
		}

		master.add(sdata);
		saveDatabase();
		reloadTable();
		return true;
	}


	public static boolean isIndexValid(String index){
		return ScoreJindex.getType(index) != ScoreJindex.CTYPE_OTHERS;
	}


	@Override
	public void inputCompleted(int code, ScoreDataFinder finder) {
		if( code == ScoreDataSearchView.CANCEL ){
			searchView.dispose();
			return;
		}

		current = master.createDataList(finder);
		current.setComparator(DEFAULT_COMPARATOR);
		reloadTable();
		searchView.dispose();
	}

	@Override
	public void inputCompleted(int code, ScoreData sdata) {
		if( code == ScoreDataEditorView.CANCEL ){
			editorView.dispose();
			return;
		}

		if( editMode == MODE_ADD ){
			if( addScoreData(sdata, editorView) ){
				editorView.dispose();
			}else{
				return;
			}
		}else if( editMode == MODE_EDIT ){
			if( setScoreData(sdata, editorView) ){
				editorView.dispose();
			}else{
				return;
			}
		}

		if( code == ScoreDataEditorView.CONTINUE ){
			if( editMode == MODE_ADD ){
				addButtonPressed();
			}else if( editMode == MODE_EDIT ){
				if( editQueue.size() > 0 ){
					editorView = createEditorView(editQueue.remove(), false);
					editorView.setVisible(true);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if( source == ctrlAdd ){
			addButtonPressed();
		}else if( source == ctrlSearch ){
			searchButtonPressed();
		}else if( source == ctrlEdit ){
			editButtonPressed();
		}else if( source == ctrlExport ){
			exportIndexHtml();
			JOptionPane.showMessageDialog(tableView, "出力が終了しました");
		}else if( source == ctrlDelete ){
			deleteButtonPressed();
		}
	}

	public void addButtonPressed(){
		editorView = createEditorView(0, true);
		editorView.setVisible(true);
		editMode = MODE_ADD;
	}

	public void searchButtonPressed(){
		searchView = new ScoreDataSearchView();
		searchView.setListener(this);
		searchView.setVisible(true);
	}

	public void editButtonPressed(){
		editQueue.clear();
		if( tableView.getSelectedRowCount() > 0 ){
			for(int row : tableView.getSelectedRows()){
				editQueue.add(current.get(row).getId());
			}
			editorView = createEditorView(editQueue.remove(), false);
			editMode = MODE_EDIT;
			editorView.setVisible(true);
		}
	}

	public void deleteButtonPressed(){
		int[] rows = tableView.getSelectedRows();
		if( rows.length > 0 ){
			int sel = JOptionPane.showConfirmDialog(tableView, new JLabel(
				rows.length + "個のデータを削除してもよろしいですか？"));
			if( sel == JOptionPane.YES_OPTION ){
				int[] ids = new int[rows.length];
				for(int i=0; i<rows.length; i++){
					ids[i] = current.get(rows[i]).getId();
				}
				for(int i=0; i<ids.length; i++){
					System.out.println("delete: " + rows[i] + ", " + ids[i]);
					current.remove(ids[i]);
					master.remove(ids[i]);
				}
			}
		}
		reloadTable();
		saveDatabase();
		return;
	}

	public ScoreDataEditorView createEditorView(int id, boolean isCreate){
		ScoreDataEditorView view;
		if( !isCreate && current.getById(id) != null ){
			view = new ScoreDataEditorView();
			ScoreData sdata = current.getById(id);
			view.setScoreData(sdata);
			view.setIdEditable(false);
		}else{
			view = new ScoreDataEditorView();
			ScoreData sdata = new ScoreData(Math.max(master.getMaxId()+1, 1001), "", "");
			view.setScoreData(sdata);
		}
		view.setListener(this);
		return view;
	}

	public void exportIndexHtml(){
		ScoreDataListContainer listc = ScoreDataListContainer
			.createDividedScoreDataList(master, ScoreJindex.getScoreDivisor());
		ScoreListContainerHtmlExporter exporter =
			new ScoreListContainerHtmlExporter(listc);
		try{
			exporter.save(new File("output.html"));
		}catch(IOException e0){
			System.err.println(e0.getMessage());
		}
	}


	public static void main(String[] args){
		ScoreDatabaseManager manager = new ScoreDatabaseManager();
	}
}
