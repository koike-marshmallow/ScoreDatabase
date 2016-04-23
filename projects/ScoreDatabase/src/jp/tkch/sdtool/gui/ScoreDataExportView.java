package jp.tkch.sdtool.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class ScoreDataExportView extends JFrame
implements ActionListener{
	public static final int APPROVE = 101;
	public static final int CANCEL = 102;
	public static final int ERROR = 109;

	public String MSG_DISCRIPTION =
		"マスターデータを出力します\n出力形式を選択して下さい";

	private String[] menu;
	private FileFilter[] filters;
	private JComboBox<String> cbType;
	private JButton btnApprove, btnCancel;
	private ScoreDataExportViewListener listener;

	public ScoreDataExportView(String[] m0){
		menu = m0;
		listener = null;
		buildFrame();
	}

	public void setListener(ScoreDataExportViewListener l0){
		listener = l0;
	}

	public void setFileFilters(FileFilter[] f0){
		filters = f0;
	}

	void buildFrame(){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 480, 160);
		JPanel pTop = new JPanel(new GridLayout(3, 1));

		pTop.add(new JLabel(MSG_DISCRIPTION));

		JPanel pCenter = new JPanel(new FlowLayout(FlowLayout.CENTER));
		cbType = new JComboBox<String>(menu);
		pCenter.add(cbType);
		pTop.add(pCenter);

		JPanel pBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnApprove = new JButton("OK");
		btnApprove.addActionListener(this);
		pBottom.add(btnApprove);
		btnCancel = new JButton("キャンセル");
		btnCancel.addActionListener(this);
		pBottom.add(btnCancel);
		pTop.add(pBottom);

		add(pTop);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if( source == btnApprove ){
			int sel = cbType.getSelectedIndex();

			JFileChooser chooser = new JFileChooser();
			if( filters != null && filters.length > sel ){
				chooser.addChoosableFileFilter(filters[sel]);
			}

			int fsel = chooser.showSaveDialog(this);
			if( fsel == JFileChooser.APPROVE_OPTION ){
				File file = chooser.getSelectedFile();
				listener.inputCompleted(APPROVE, sel, file);
			}else if( fsel == JFileChooser.CANCEL_OPTION ){
				listener.inputCompleted(CANCEL, sel, null);
			}else if( fsel == JFileChooser.ERROR_OPTION ){
				JOptionPane.showMessageDialog(this, new JLabel("file choose error"));
				listener.inputCompleted(ERROR, sel, null);
			}
		}else if( source == btnCancel ){
			listener.inputCompleted(CANCEL, -1, null);
		}

	}
}
