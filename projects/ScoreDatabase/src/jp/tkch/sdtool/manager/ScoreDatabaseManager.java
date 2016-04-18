package jp.tkch.sdtool.manager;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import jp.tkch.sdtool.gui.ScoreDataEditorView;
import jp.tkch.sdtool.gui.ScoreDataEditorViewListener;
import jp.tkch.sdtool.gui.ScoreDataListTableModel;
import jp.tkch.sdtool.gui.ScoreDataSearchView;
import jp.tkch.sdtool.gui.ScoreDataSearchViewListener;
import jp.tkch.sdtool.gui.ScoreTableView;
import jp.tkch.sdtool.model.ScoreData;
import jp.tkch.sdtool.model.ScoreDataFinder;
import jp.tkch.sdtool.model.ScoreDataList;
import jp.tkch.sdtool.model.comparator.ScoreDataIdComparator;

public class ScoreDatabaseManager
implements ActionListener, ScoreDataEditorViewListener,
ScoreDataSearchViewListener{
	public static final int MODE_ADD = 1001;
	public static final int MODE_EDIT = 1002;

	private ScoreDataList master;
	private ScoreDataList current;
	private ScoreTableView tableView;
	private ScoreDataEditorView editorView;
	private ScoreDataSearchView searchView;
	private JButton ctrlAdd, ctrlSearch, ctrlEdit;
	private Queue<Integer> editQueue;
	private int editMode;

	public ScoreDatabaseManager(){
		master = new ScoreDataList(new ScoreDataIdComparator());
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
		tableView.addControlComponent(ctrlAdd);
		tableView.addControlComponent(ctrlSearch);
		tableView.addControlComponent(ctrlEdit);
		reloadTable();
		tableView.setVisible(true);
	}

	public void reloadTable(){
		tableView.setScoreDataListTableModel(new ScoreDataListTableModel(current));
	}


	public boolean addScoreData(ScoreData sdata, Component parent){
		if( master.isIdRegisted(sdata.getId()) ){
			if( parent != null ){
				JOptionPane.showMessageDialog(parent, new JLabel("idがすでに登録されています"));
			}
			return false;
		}
		if( !isIndexValid(sdata.getIndex()) ){
			if( parent != null ){
				JOptionPane.showMessageDialog(parent, new JLabel("よみがなに無効な文字が含まれています"));
			}
			return false;
		}

		master.add(sdata);
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
			if( parent != null ){
				JOptionPane.showMessageDialog(parent, new JLabel("よみがなに無効な文字が含まれています"));
			}
			return false;
		}

		master.add(sdata);
		reloadTable();
		return true;
	}


	public static boolean isIndexValid(String index){
		return true;
	}


	@Override
	public void inputCompleted(int code, ScoreDataFinder finder) {
		if( code == ScoreDataSearchView.CANCEL ){
			searchView.dispose();
			return;
		}

		current = master.createDataList(finder);
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
					editorView = createEditorView(editQueue.remove());
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
		}
	}

	public void addButtonPressed(){
		editorView = createEditorView(-1);
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
			for(Integer row : tableView.getSelectedRows()){
				editQueue.add(row);
			}
			editorView = createEditorView(editQueue.remove());
			editMode = MODE_EDIT;
			editorView.setVisible(true);
		}
	}

	public ScoreDataEditorView createEditorView(int row){
		ScoreDataEditorView view;
		if( row >= 0 && row < tableView.getRowCount() ){
			view = new ScoreDataEditorView();
			ScoreData sdata = current.get(row);
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


	public static void main(String[] args){
		ScoreDatabaseManager manager = new ScoreDatabaseManager();
	}
}
