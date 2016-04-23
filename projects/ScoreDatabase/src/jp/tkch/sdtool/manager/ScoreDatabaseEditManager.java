package jp.tkch.sdtool.manager;

import java.awt.Component;
import java.util.ArrayDeque;
import java.util.Queue;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import jp.tkch.sdtool.gui.ScoreDataEditorView;
import jp.tkch.sdtool.gui.ScoreDataEditorViewListener;
import jp.tkch.sdtool.jindex.ScoreJindex;
import jp.tkch.sdtool.model.ScoreData;
import jp.tkch.sdtool.model.ScoreDataList;

public class ScoreDatabaseEditManager
implements ScoreDataEditorViewListener{
	public static String ERRMSG_ID_NOTFOUND = "idが登録されていません";
	public static String ERRMSG_ID_DUPLICATION = "idが既に登録されています";
	public static String CONFIRM_INDEX_INVALID =
		"ヨミガナに無効な文字が含まれています\nこのまま登録しますか？";
	public static String CONFIRM_DELETE =
		"件のデータを削除します\nよろしいですか？";

	private Component parent;
	private ScoreDataEditorView editorView;
	private Queue<Integer> dataIdQueue;
	private ScoreDataList master;
	private boolean isEdit;
	private ReloadListener rlistener;

	public ScoreDatabaseEditManager(Component p0, ScoreDataList m0){
		parent = p0;
		editorView = null;
		master = m0;
		dataIdQueue = new ArrayDeque<Integer>();
		isEdit = false;
		rlistener = null;
	}

	public void enqueue(int id){
		if( master.isIdRegisted(id) ){
			dataIdQueue.add(id);
		}
	}

	public void setListener(ReloadListener l0){
		rlistener = l0;
	}

	ScoreDataEditorView createEditorView(int id, boolean isCreate){
		ScoreDataEditorView view = new ScoreDataEditorView();

		if( isCreate || !master.isIdRegisted(id) ){
			int nextId = Math.max(master.getMaxId()+1, 1001);
			view.setScoreData(new ScoreData(nextId, "", ""));
		}else{
			view.setScoreData(master.getById(id));
			view.setIdEditable(false);
		}

		view.setListener(this);

		return view;
	}

	void showEditorView(){
		if( isEdit ){
			if( dataIdQueue.size() > 0 ){
				editorView = createEditorView(dataIdQueue.remove(), false);
			}else{
				return;
			}
		}else{
			editorView = createEditorView(0, true);
		}

		editorView.setListener(this);
		editorView.setVisible(true);
	}

	boolean assertScoreDataSettable(ScoreData data){
		if( !master.isIdRegisted(data.getId()) ){
			if( parent != null ){
				JOptionPane.showMessageDialog(
					parent, new JLabel(ERRMSG_ID_NOTFOUND));
			}
			return false;
		}

		if( !assertScoreDataIndexValid(data) ){
			return false;
		}

		return true;
	}

	boolean assertScoreDataAddable(ScoreData data){
		if( master.isIdRegisted(data.getId()) ){
			if( parent != null ){
				JOptionPane.showMessageDialog(
					parent, new JLabel(ERRMSG_ID_DUPLICATION));
			}
			return false;
		}

		if( !assertScoreDataIndexValid(data) ){
			return false;
		}

		return true;
	}

	boolean assertScoreDataIndexValid(ScoreData data){
		if( ScoreJindex.getType(data.getIndex()) == ScoreJindex.CTYPE_OTHERS ){
			if( parent != null ){
				int sel = JOptionPane.showConfirmDialog(
					parent, new JLabel(CONFIRM_INDEX_INVALID));
				if( sel == JOptionPane.YES_OPTION ){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
		return true;
	}


	void setScoreData(ScoreData data){
		master.add(data);
	}

	void addScoreData(ScoreData data){
		master.add(data);
	}

	void reload(){
		if( rlistener != null ){
			rlistener.reload();
		}
	}


	public void excuteAddMode(){
		isEdit = false;
		showEditorView();
	}

	public void excuteEditMode(){
		isEdit = true;
		showEditorView();
	}

	public void excuteDeleteMode(){
		if( dataIdQueue.size() > 0 ){
			int sel = JOptionPane.showConfirmDialog(
				parent, new JLabel(dataIdQueue.size() + CONFIRM_DELETE));

			if( sel == JOptionPane.YES_OPTION ){
				while( !dataIdQueue.isEmpty() ){
					master.remove(dataIdQueue.remove());
				}
			}
		}

		reload();
	}

	@Override
	public void inputCompleted(int code, ScoreData sdata) {
		if(
			code == ScoreDataEditorView.CANCEL
		){
			return;
		}

		if(
			code == ScoreDataEditorView.REGIST ||
			code == ScoreDataEditorView.CONTINUE
		){
			if( isEdit ){
				if( !assertScoreDataSettable(sdata) ){
					return;
				}
				setScoreData(sdata);
			}else{
				if( !assertScoreDataAddable(sdata) ){
					return;
				}
				addScoreData(sdata);
			}
			editorView.dispose();
			reload();
		}

		if(
			code == ScoreDataEditorView.CONTINUE
		){
			if( isEdit ){
				if( dataIdQueue.size() > 0 ){
					showEditorView();
				}
			}else{
				showEditorView();
			}
		}
	}
}
