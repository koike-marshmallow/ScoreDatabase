package jp.tkch.sdtool.manager;

import jp.tkch.sdtool.gui.ScoreDataSearchView;
import jp.tkch.sdtool.gui.ScoreDataSearchViewListener;
import jp.tkch.sdtool.model.ScoreDataFinder;

public class ScoreDatabaseSearchManager
implements ScoreDataSearchViewListener {
	private DualScoreDataList dlist;
	private ScoreDataSearchView searchView;
	private ReloadListener listener;

	public ScoreDatabaseSearchManager(DualScoreDataList d0){
		dlist = d0;
		searchView = null;
	}

	public void setListener(ReloadListener l0){
		listener = l0;
	}

	void showEditorView(){
		searchView = new ScoreDataSearchView();
		searchView.setListener(this);
		searchView.setVisible(true);
	}

	@Override
	public void inputCompleted(int code, ScoreDataFinder finder) {
		if( code == ScoreDataSearchView.EXCUTE ){
			dlist.setCurrent(finder);
			listener.reload();
		}
		searchView.dispose();
	}
}
