package jp.tkch.sdtool.exporter;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import jp.tkch.htmlbuilder.css.CSSBlock;
import jp.tkch.htmlbuilder.css.CSSBlockList;
import jp.tkch.htmlbuilder.css.CSSProperty;
import jp.tkch.sdtool.model.ScoreData;
import jp.tkch.sdtool.model.ScoreDataList;

public class ScoreListHtmlExporter {
	private ScoreDataList list;
	private String label;
	private Document doc;


	public ScoreListHtmlExporter(){
		list = null;
		label = null;
		doc = null;
	}

	public ScoreListHtmlExporter(ScoreDataList l0){
		this();
		setScoreDataList(l0);
	}

	public ScoreListHtmlExporter(ScoreDataList l0, String l1){
		this();
		setScoreDataList(l0);
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

	public Element createStyleElement(){
		CSSBlockList cssList = new CSSBlockList();

		cssList.add(new CSSBlock("body")
			.addProperty(new CSSProperty("width", "800px")));
		cssList.add(new CSSBlock("th,td")
			.addProperty(new CSSProperty("border", "1px #000 solid"))
			.addProperty(new CSSProperty("padding", "4px")));
		cssList.add(new CSSBlock("table")
			.addProperty(new CSSProperty("border-collapse", "collapse")));

		Element eStyle = doc.createElement("style");
		eStyle.setTextContent(cssList.toString());

		return eStyle;
	}

	public Element createHead(){
		String title = "楽譜一覧";
		if( label != null ){
			title += " - " + label;
		}

		Element eHead = doc.createElement("head");
		Element ePTitle = doc.createElement("title");
		ePTitle.setTextContent(title);
		eHead.appendChild(ePTitle);
		eHead.appendChild(createStyleElement());

		return eHead;
	}

	public Element createBody(){
		Element eMark = doc.createElement("p");
		eMark.setTextContent("楽譜データベース");
		Element eTitle = doc.createElement("h1");
		eTitle.setTextContent("楽譜一覧");
		Element eSubTitle = null;
		if( label != null ){
			eSubTitle = doc.createElement("h2");
			eSubTitle.setTextContent(label);
		}
		Element eTable =
			createScoreListTableElement(doc, list);

		Element eBody = doc.createElement("body");
		eBody.appendChild(eMark)
			.appendChild(eTitle);
		if( eSubTitle != null ){
			eBody.appendChild(eSubTitle);
		}
		eBody.appendChild(eTable);

		return eBody;
	}

	public boolean build(){
		doc = createDocument();

		if( doc == null || list == null ){
			return false;
		}

		Element root = doc.getDocumentElement();
		root.appendChild(createHead());
		root.appendChild(createBody());

		return true;
	}

	public boolean save(File file)
	throws IOException{
		TransformerFactory tff = TransformerFactory.newInstance();
		Transformer tf;
		try{
			tf = tff.newTransformer();
		}catch(TransformerConfigurationException e0){
			return false;
		}
		tf.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		tf.setOutputProperty("indent", "yes");
		tf.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");

		if( !build() ) return false;

		if( doc != null ){
			StreamResult target = new StreamResult(file);
			try{
				tf.transform(new DOMSource(doc), target);
			}catch(TransformerException e0){
				return false;
			}
		}

		return true;
	}


	public static Document createDocument(){
		DocumentBuilderFactory dbf;
		DocumentBuilder db;
		try{
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
		}catch(ParserConfigurationException e0){
			return null;
		}

		DOMImplementation impl = db.getDOMImplementation();
		DocumentType dtype = impl.createDocumentType(
			"html",
			"-//W3C//DTD XHTML 1.0 Strict//EN",
			"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");

		return impl.createDocument("", "html", dtype);
	}

	public static Element createScoreListTableElement(Document doc, ScoreDataList list){
		Element eTable = doc.createElement("table");
		String[] heads = {"ID", "タイトル", "編曲者/作曲者", "出版社", "備考"};
		String[] width = {"50px", "280px", "125px", "125px", "220px"};
		eTable.appendChild(convertToTableRowElement(doc, heads, width, true));

		for(int i=0; i<list.getDataCount(); i++){
			eTable.appendChild(createScoreDataRowElement(doc, list.get(i)));
		}

		return eTable;
	}

	public static Element createScoreDataRowElement(Document doc, ScoreData data){
		String[] strs = new String[5];
		strs[0] = String.valueOf(data.getId());
		strs[1] = data.getTitle();
		strs[2] = data.getParameter("author");
		strs[3] = data.getParameter("publisher");
		strs[4] = data.getParameter("comment");
		return convertToTableRowElement(doc, strs, null, false);
	}

	public static Element convertToTableRowElement
	(Document doc, String[] strs, String[] width,  boolean isTh){
		Element eTr = doc.createElement("tr");
		String tagName = isTh ? "th" : "td";
		for(int i=0; i<strs.length; i++){
			Element eTd = doc.createElement(tagName);
			eTd.setTextContent(strs[i]);
			if( width != null && width.length > i ){
				eTd.setAttribute("style", "width:" + width[i] + ";");
			}
			eTr.appendChild(eTd);
		}
		return eTr;
	}

	public static void main(String[] args)
	throws IOException{
		ScoreDataList list = new ScoreDataList();

		list.add(new ScoreData(1001, "He is a pirate", "He is a pirate"));
		list.add(new ScoreData(1002, "My Heart Will Go On", "My Heart Will Go On"));

		ScoreListHtmlExporter exporter = new ScoreListHtmlExporter(list, "テスト");
		exporter.save(new File("list.html"));
	}
}
