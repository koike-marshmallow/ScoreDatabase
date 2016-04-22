package jp.tkch.sdtool.exporter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jp.tkch.sdtool.model.ScoreData;
import jp.tkch.sdtool.model.ScoreDataList;

public class ScoreListTableHtmlBuilder {
	static String[] HEAD_LABELS = {"ID", "タイトル", "作曲者/編曲者", "出版社", "備考"};
	public static int[] DEFAULT_WIDTH = {50, 280, 125, 125, 220};

	private ScoreDataList list;
	private Document doc;
	private String label;
	private int[] width;

	public ScoreListTableHtmlBuilder(){
		list = null;
		label = null;
		width = null;
		doc = null;
	}

	public ScoreListTableHtmlBuilder(Document d0){
		this();
		setDocument(d0);
	}

	public ScoreListTableHtmlBuilder(Document d0, ScoreDataList l0){
		this(d0);
		setScoreDataList(l0);
	}

	public ScoreListTableHtmlBuilder(Document d0, ScoreDataList l0, String l1){
		this(d0, l0);
		setLabel(l1);
	}

	public void setScoreDataList(ScoreDataList l0){
		list = l0;
	}

	public ScoreDataList getScoreDataList(){
		return list;
	}

	public void setLabel(String l0){
		label = l0;
	}

	public String getLabel(){
		return label;
	}

	public void setWidthConfig(int[] wc){
		width = wc;
	}

	public int[] getWidthConfig(){
		return width;
	}

	public void setDocument(Document d0){
		doc = d0;
	}

	public Document getDocument(){
		return doc;
	}

	public Element buildScoreListTable(){
		if( doc == null || list == null ){
			return null;
		}

		Element eDiv = doc.createElement("div");

		if( label != null ){
			Element eLabel = doc.createElement("h2");
			eLabel.setTextContent(label);
			eDiv.appendChild(eLabel);
		}

		Element eTable = doc.createElement("table");
		eTable.appendChild(
			createTableRowElement(doc, HEAD_LABELS, width, true));
		for(int i=0; i<list.getDataCount(); i++){
			eTable.appendChild(
				createScoreDataRowElement(doc, list.get(i)));
		}
		eDiv.appendChild(eTable);

		return eDiv;
	}

	public static Element createTableRowElement
	(Document tdoc, String[] values, int[] width, boolean isTh){
		String tagName = isTh ? "th" : "td";
		Element eTr = tdoc.createElement("tr");
		for(int i=0; i<values.length; i++){
			Element eTd = tdoc.createElement(tagName);
			if( width != null && width.length > i ){
				eTd.setAttribute("style", "width:" + width[i] + "px;");
			}
			eTd.setTextContent(values[i]);
			eTr.appendChild(eTd);
		}
		return eTr;
	}

	public static Element createScoreDataRowElement(Document tdoc, ScoreData data){
		String[] values = new String[5];
		values[0] = String.valueOf(data.getId());
		values[1] = data.getTitle();
		values[2] = data.getParameter("author");
		values[3] = data.getParameter("publisher");
		values[4] = data.getParameter("comment");
		return createTableRowElement(tdoc, values, null, false);
	}
}
