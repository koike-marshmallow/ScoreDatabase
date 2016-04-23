package jp.tkch.sdtool.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import jp.tkch.sdtool.model.ScoreData;
import jp.tkch.sdtool.model.ScoreDataList;
import jp.tkch.sdtool.model.comparator.ScoreDataIdComparator;
import jp.tkch.sdtool.model.comparator.ScoreDataIndexComparator;

public class ScoreTableView extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel pTop;
	private JPanel pControl;
	private JTable tScoreTable;
	private JLabel itemCount;

	// ウィンドウ本体
	public ScoreTableView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 480);

		pTop = new JPanel(new BorderLayout());
		pControl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pTop.add(pControl, BorderLayout.PAGE_START);
		tScoreTable = new JTable(10, 4);
		pTop.add(new JScrollPane(tScoreTable), BorderLayout.CENTER);
		itemCount = new JLabel("項目数: 0");
		pTop.add(itemCount, BorderLayout.PAGE_END);
		add(pTop);
	}

	public void addControlComponent(Component comp){
		pControl.add(comp);
	}

	public void setScoreDataListTableModel(ScoreDataListTableModel model){
		tScoreTable.setModel(model);
		itemCount.setText("項目数: " + model.getRowCount());
	}

	public boolean isRowSelected(int row){
		return tScoreTable.isRowSelected(row);
	}

	public int[] getSelectedRows(){
		return tScoreTable.getSelectedRows();
	}

	public int getSelectedRowCount(){
		return tScoreTable.getSelectedRowCount();
	}

	public int getRowCount(){
		return tScoreTable.getRowCount();
	}

	public void setStatusMessage(String msg){
		itemCount.setText(msg);
	}

	public void alert(String msg){
		JOptionPane.showMessageDialog(this, new JLabel(msg));
	}

    public static void main(String[] args) {
        ScoreTableView frm = new ScoreTableView();   // ウィンドウ作成
        JButton btnAdd = new JButton("追加");
        frm.addControlComponent(btnAdd);
        frm.addControlComponent(new JButton("編集"));
        ScoreDataList list = new ScoreDataList(new ScoreDataIndexComparator());
        btnAdd.addActionListener(new DefaultActionListener(frm, list));
        list.add(new ScoreData(1001, "He is a pirate", "He is a pirate"));
        list.add(new ScoreData(1002, "My Heart Will Go On", "My Heart Will Go On"));
        list.add(new ScoreData(1003, "夢への冒険", "ユメヘノボウケン"));
        printList(list);
        frm.setScoreDataListTableModel(new ScoreDataListTableModel(list));
        frm.setVisible(true);  // 表示
        list.add(new ScoreData(1004, "マーチ「プロヴァンスの風」", "プロヴァンスノカゼ"));
    }

    public static void printList(ScoreDataList list){
        for(int i=0; i<list.getDataCount(); i++){
        	System.out.println(list.get(i).getId() + ": " + list.get(i).getTitle());
        }
    }

}


class DefaultActionListener implements ActionListener{

	ScoreDataList list;
	ScoreTableView view;

	public DefaultActionListener(ScoreTableView view, ScoreDataList list){
		this.list = list;
		this.view = view;
	}

 	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("performerd");
		list.setComparator(new ScoreDataIdComparator());
		view.setScoreDataListTableModel(new ScoreDataListTableModel(list));
	}


}
