package jp.tkch.sdtool.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import jp.tkch.sdtool.model.ScoreDataFinder;
import jp.tkch.sdtool.model.finder.ScoreDataKeywordFinder;

public class ScoreDataSearchView extends JFrame
implements ActionListener{
	public static final int EXCUTE = 101;
	public static final int CANCEL = 102;

	JTextField tfKeywords;
	ButtonGroup bgLogicSelect;
	JRadioButton radioOr, radioAnd;
	JCheckBox cbTitle, cbAuthor, cbPublisher, cbComment;
	JButton btnSearch, btnCancel;

	ScoreDataSearchViewListener listener;


	public ScoreDataSearchView(){
		buildFrame();
	}

	private void buildFrame(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 320);

		JPanel pTop = new JPanel(new BorderLayout());
		pTop.add(new Label("楽譜を検索します"), BorderLayout.PAGE_START);

		JPanel pForm = new JPanel(new GridLayout(3, 1));
		JPanel pForm1 = new JPanel();
		JPanel pForm2 = new JPanel();
		JPanel pForm3 = new JPanel();
		tfKeywords = new JTextField("", 20);
		bgLogicSelect= new ButtonGroup();
		radioOr = new JRadioButton("OR", true);
		radioAnd = new JRadioButton("AND");
		bgLogicSelect.add(radioOr);
		bgLogicSelect.add(radioAnd);
		cbTitle = new JCheckBox("タイトル", true);
		cbAuthor = new JCheckBox("編曲/作曲者", true);
		cbPublisher = new JCheckBox("出版社");
		cbComment = new JCheckBox("コメント");
		pForm1.add(new Label("キーワード"));
		pForm1.add(tfKeywords);
		pForm2.add(new Label("論理結合"));
		pForm2.add(radioOr);
		pForm2.add(radioAnd);
		pForm3.add(new Label("対象"));
		pForm3.add(cbTitle);
		pForm3.add(cbAuthor);
		pForm3.add(cbPublisher);
		pForm3.add(cbComment);
		pForm.add(pForm1);
		pForm.add(pForm2);
		pForm.add(pForm3);

		pTop.add(pForm, BorderLayout.CENTER);

		JPanel pButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnSearch = new JButton("検索");
		btnCancel = new JButton("キャンセル");
		btnSearch.addActionListener(this);
		btnCancel.addActionListener(this);
		pButton.add(btnSearch);
		pButton.add(btnCancel);

		pTop.add(pButton, BorderLayout.PAGE_END);

		add(pTop);
	}

	public void setListener(ScoreDataSearchViewListener l0){
		listener = l0;
	}


	public ScoreDataFinder getScoreDataFinder(){
		ScoreDataKeywordFinder finder = new ScoreDataKeywordFinder();
		finder.addKeywords(tfKeywords.getText());
		finder.setTarget(ScoreDataKeywordFinder.TARGET_TITLE, cbTitle.isSelected());
		finder.setTarget(ScoreDataKeywordFinder.TARGET_AUTHOR, cbAuthor.isSelected());
		finder.setTarget(ScoreDataKeywordFinder.TARGET_PUBLISHER, cbPublisher.isSelected());
		finder.setTarget(ScoreDataKeywordFinder.TARGET_COMMENT, cbComment.isSelected());
		if( radioAnd.isSelected() ){
			finder.setLogic(ScoreDataKeywordFinder.LOGIC_AND);
		}else{
			finder.setLogic(ScoreDataKeywordFinder.LOGIC_OR);
		}
		return finder;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		int code = -1;
		Object source = e.getSource();
		if( source == btnSearch ){
			code = EXCUTE;
		}else if(source == btnCancel ){
			code = CANCEL;
		}

		if( listener != null ){
			listener.inputCompleted(code, getScoreDataFinder());
		}
	}

	public static void main(String[] args){
		ScoreDataSearchView view = new ScoreDataSearchView();
		view.setListener(new ScoreDataSearchViewListener(){
			public void inputCompleted(int c, ScoreDataFinder finder){
				System.out.println("[" + c + "]" + finder.toString());
				view.dispose();
			}
		});
		view.setVisible(true);
	}

}
