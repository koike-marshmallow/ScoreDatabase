package jp.tkch.sdtool.gui;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import jp.tkch.sdtool.model.ScoreDataList;

public class ScoreDataListTableModel implements TableModel {
	public static String[] columnLabels = {
		"ID", "タイトル", "作曲者/編曲者", "出版社", "備考"
	};

	private ScoreDataList list;

	public ScoreDataListTableModel(){
		list = null;
	}

	public ScoreDataListTableModel(ScoreDataList l0){
		list = l0;
	}

	@Override
	public int getRowCount() {
		return list.getDataCount();
	}

	@Override
	public int getColumnCount() {
		return columnLabels.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if( columnIndex >= 0 && columnIndex <= columnLabels.length ){
			return columnLabels[columnIndex];
		}
		return "";
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public String getValueAt(int rowIndex, int columnIndex) {
		if( rowIndex < 0 || rowIndex >= list.getDataCount() ){
			return "";
		}
		switch( columnIndex ){
		case 0:
			return String.valueOf(list.get(rowIndex).getId());
		case 1:
			return list.get(rowIndex).getTitle();
		case 2:
			return list.get(rowIndex).getParameter("author");
		case 3:
			return list.get(rowIndex).getParameter("publisher");
		case 4:
			return list.get(rowIndex).getParameter("comment");
		default:
			return "";
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
