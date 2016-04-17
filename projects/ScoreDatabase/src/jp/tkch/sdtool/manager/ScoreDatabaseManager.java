package jp.tkch.sdtool.manager;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private JButton ctrlAdd, ctrlSearch;
	private int editMode;

	public ScoreDatabaseManager(){
		master = new ScoreDataList();
		current = master;
		init();
	}

	public void init(){
		tableView = new ScoreTableView();
		ctrlAdd = new JButton("追加");
		ctrlAdd.addActionListener(this);
		ctrlSearch = new JButton("検索");
		ctrlSearch.addActionListener(this);
		tableView.addControlComponent(ctrlAdd);
		tableView.addControlComponent(ctrlSearch);
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
		if( master.isIdRegisted(sdata.getId()) ){
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
			}
		}else if( editMode == MODE_EDIT ){
			if( setScoreData(sdata, editorView) ){
				editorView.dispose();
			}
		}

		if( code == ScoreDataEditorView.CONTINUE ){
			if( editMode == MODE_ADD ){
				addButtonPressed();
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
		}
	}

	public void addButtonPressed(){
		editorView = new ScoreDataEditorView();
		editorView.setScoreData(new ScoreData(
			Math.max(master.getMaxId()+1, 1001), "", ""));
		editorView.setListener(this);
		editorView.setVisible(true);
		editMode = MODE_ADD;
	}

	public void searchButtonPressed(){
		searchView = new ScoreDataSearchView();
		searchView.setListener(this);
		searchView.setVisible(true);
	}


	public static void main(String[] args){
		ScoreDatabaseManager manager = new ScoreDatabaseManager();
	}




}
