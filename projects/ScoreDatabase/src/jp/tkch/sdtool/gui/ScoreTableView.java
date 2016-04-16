package jp.tkch.sdtool.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import jp.tkch.sdtool.model.ScoreData;
import jp.tkch.sdtool.model.ScoreDataList;

public class ScoreTableView extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel pTop;
	private JPanel pControl;
	private JTable tScoreTable;
	private ScoreDataListTableModel tableModel;

	// ウィンドウ本体
	public ScoreTableView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 480);

		pTop = new JPanel(new BorderLayout());
		pControl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pTop.add(pControl, BorderLayout.PAGE_START);
		tScoreTable = new JTable(10, 4);
		pTop.add(new JScrollPane(tScoreTable), BorderLayout.CENTER);
		add(pTop);
	}

	public void addControlComponent(Component comp){
		pControl.add(comp);
	}

	public void setScoreDataListTableModel(ScoreDataListTableModel model){
		tableModel = model;
		tScoreTable = new JTable(tableModel);
		pTop.add(new JScrollPane(tScoreTable), BorderLayout.CENTER);
	}

    public static void main(String[] args) {
        ScoreTableView frm = new ScoreTableView();   // ウィンドウ作成
        frm.addControlComponent(new JButton("追加"));
        frm.addControlComponent(new JButton("編集"));
        ScoreDataList list = new ScoreDataList();
        list.add(new ScoreData(1001, "He is a pirate", "He is a pirate"));
        list.add(new ScoreData(1002, "My Heart Will Go On", "My Heart Will Go On"));
        list.add(new ScoreData(1003, "夢への冒険", "ユメヘノボウケン"));
        for(int i=0; i<list.getDataCount(); i++){
        	System.out.println(list.get(i).getId() + ": " + list.get(i).getTitle());
        }
        frm.setScoreDataListTableModel(new ScoreDataListTableModel(list));
        frm.setVisible(true);  // 表示
        list.add(new ScoreData(1004, "マーチ「プロヴァンスの風」", "プロヴァンスの風"));
    }

}