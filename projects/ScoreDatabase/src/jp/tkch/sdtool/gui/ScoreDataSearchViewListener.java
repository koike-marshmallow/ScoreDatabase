package jp.tkch.sdtool.gui;

import jp.tkch.sdtool.model.ScoreDataFinder;

public interface ScoreDataSearchViewListener {
	public void inputCompleted(int code, ScoreDataFinder finder);
}
