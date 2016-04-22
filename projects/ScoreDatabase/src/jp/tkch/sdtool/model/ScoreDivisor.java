package jp.tkch.sdtool.model;

public interface ScoreDivisor {
	public static final int GROUP_UNDEFINED = -1;

	public boolean isValid(ScoreData data);
	public int groupNumberOf(ScoreData data);
	public int getGroupCount();
	public String getGroupLabel(int num);
}
