package jp.tkch.sdtool.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jp.tkch.sdtool.model.ScoreData;

public class ScoreDataEditorView extends JFrame
implements ActionListener{
	public static final int REGIST = 101;
	public static final int CANCEL = 102;
	public static final int CONTINUE = 103;

	static String[] fieldLabels = {
		"ID", "タイトル", "ヨミガナ", "作曲者/編曲者", "出版社", "コメント"
	};
	static String[] buttonLabels = {
		"確定", "続ける", "キャンセル"
	};

	private JTextField[] tfields;
	private JButton[] buttons;
	private ScoreDataEditorViewListener listener;



	public ScoreDataEditorView() {
		buildFrame();
	}

	public ScoreDataEditorView(ScoreData sdata){
		this();
		setScoreData(sdata);
	}

	public ScoreDataEditorView(ScoreData sdata, ScoreDataEditorViewListener l0){
		this(sdata);
		setListener(l0);
	}

	private void buildFrame(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 320);

		JPanel pTop = new JPanel(new BorderLayout());
		pTop.add(new Label("楽譜の情報を入力します"), BorderLayout.PAGE_START);

		JPanel pInput = new JPanel(new GridLayout(fieldLabels.length, 2));
		createField();
		for(int i=0; i<tfields.length; i++){
			pInput.add(new JLabel(fieldLabels[i]));
			pInput.add(tfields[i]);
		}
		pTop.add(pInput, BorderLayout.CENTER);

		JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		createButtons();
		for(int i=0; i<buttons.length; i++){
			pBtn.add(buttons[i]);
		}
		pTop.add(pBtn, BorderLayout.PAGE_END);

		add(pTop);
	}

	private void createField(){
		tfields = new JTextField[fieldLabels.length];
		for(int i=0; i<tfields.length; i++){
			tfields[i] = new JTextField();
		}
	}

	private void createButtons(){
		buttons = new JButton[buttonLabels.length];
		for(int i=0; i<buttons.length; i++){
			buttons[i] = new JButton(buttonLabels[i]);
			buttons[i].addActionListener(this);
		}
	}

	public void setScoreData(ScoreData sd){
		tfields[0].setText(String.valueOf(sd.getId()));
		tfields[1].setText(sd.getTitle());
		tfields[2].setText(sd.getIndex());
		tfields[3].setText(sd.getParameter("author"));
		tfields[4].setText(sd.getParameter("publisher"));
		tfields[5].setText(sd.getParameter("comment"));
	}

	public ScoreData getScoreData(){
		ScoreData input = new ScoreData();
		try{
			input.setId(Integer.parseInt(tfields[0].getText()));
		}catch(NumberFormatException e){
			input.setId(0);
		}
		input.setTitle(tfields[1].getText());
		input.setIndex(tfields[2].getText());
		input.setParameter("author", tfields[3].getText());
		input.setParameter("publisher", tfields[4].getText());
		input.setParameter("comment", tfields[5].getText());

		return input;
	}

	public void setListener(ScoreDataEditorViewListener l0){
		listener = l0;
	}


	public void setIdEditable(boolean b0){
		tfields[0].setEditable(b0);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		int code = 0;
		Object source = e.getSource();

		if( source == buttons[0] ){
			code = REGIST;
		}else if( source == buttons[1] ){
			code = CONTINUE;
		}else if( source == buttons[2] ){
			code = CANCEL;
		}

		if( listener != null ){
			listener.inputCompleted(code, getScoreData());
		}
	}


	public static void main(String[] args){
		ScoreDataEditorView view = new ScoreDataEditorView(new ScoreData(1001, "ユメヘノボウケン"));
		view.setListener(new ScoreDataEditorViewListener(){
			public void inputCompleted(int c, ScoreData sd){
				System.out.println("[" + c + "] " + sd.getTitle());
			}
		});
		view.setVisible(true);
	}
}
