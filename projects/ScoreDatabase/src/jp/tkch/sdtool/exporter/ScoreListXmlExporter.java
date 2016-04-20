package jp.tkch.sdtool.exporter;

import java.io.File;
import java.io.FileInputStream;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jp.tkch.sdtool.model.ScoreData;
import jp.tkch.sdtool.model.ScoreDataList;
import jp.tkch.sdtool.model.ScoreDataParameter;

public class ScoreListXmlExporter {
	private ScoreDataList list;
	private Document doc;

	public ScoreListXmlExporter(){
		list = null;
		doc = null;
	}

	public ScoreListXmlExporter(ScoreDataList l0){
		this();
		setScoreDataList(l0);
	}

	public boolean build(){
		doc = null;
		if( list != null ){
			try{
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				doc = db.newDocument();
			}catch(ParserConfigurationException e0){
				doc = null;
				return false;
			}

			Element eRoot = doc.createElement("scores");
			doc.appendChild(eRoot);

			for(int i=0; i<list.getDataCount(); i++){
				Element data = scoreDataToElement(doc, list.get(i));
				if( data != null ){
					eRoot.appendChild(data);
				}
			}
		}
		return true;
	}

	public boolean parse(){
		list = new ScoreDataList();

		Element eRoot = doc.getDocumentElement();
		if( eRoot.getTagName().equals("scores") ){
			NodeList nlist = eRoot.getElementsByTagName("score");
			for(int i=0; i<nlist.getLength(); i++){
				ScoreData data = elementToScoreData((Element)nlist.item(i));
				if( data != null ){
					list.add(data);
				}
			}
		}

		return true;
	}

	public boolean load(File file)
	throws IOException{
		DocumentBuilderFactory dbf;
		DocumentBuilder db;
		try{
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
		}catch(ParserConfigurationException e0){
			doc = null;
			list = null;
			return false;
		}

		FileInputStream stream = new FileInputStream(file);
		try{
			doc = db.parse(stream);
		}catch(SAXException e0){
			doc = null;
			list = null;
			return false;
		}

		if( !parse() ) return false;

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

	public void setScoreDataList(ScoreDataList l0){
		list = l0;
	}

	public ScoreDataList getScoreDataList(){
		return list;
	}

	public static Element scoreDataToElement(Document doc, ScoreData sdata){
		Element eScore = doc.createElement("score");
		eScore.setAttribute("id", String.valueOf(sdata.getId()));

		Element eTitle = doc.createElement("title");
		eTitle.setAttribute("value", sdata.getTitle());
		eScore.appendChild(eTitle);
		Element eIndex = doc.createElement("index");
		eIndex.setAttribute("value", sdata.getIndex());
		eScore.appendChild(eIndex);

		for(ScoreDataParameter param : sdata.getParameters()){
			Element eParamTmp = doc.createElement("param");
			eParamTmp.setAttribute("key", param.getKey());
			eParamTmp.setAttribute("value", param.getValue());
			eScore.appendChild(eParamTmp);
		}

		return eScore;
	}

	public static ScoreData elementToScoreData(Element elem){
		ScoreData sdata = new ScoreData();
		if( elem.getNodeType() != Node.ELEMENT_NODE &&
			!elem.getTagName().equals("score") ){
			return null;
		}
		try{
			int id = Integer.parseInt(elem.getAttribute("id"));
			sdata.setId(id);
		}catch(NumberFormatException e0){
			return null;
		}
		for(Node nd = elem.getFirstChild();
			nd != null; nd = nd.getNextSibling()){
			if( nd.getNodeType() == Node.ELEMENT_NODE ){
				Element etmp = (Element)nd;
				String tagName = etmp.getTagName();
				if( tagName.equals("title") ){
					sdata.setTitle(etmp.getAttribute("value"));
				}else if( tagName.equals("index") ){
					sdata.setIndex(etmp.getAttribute("value"));
				}else if( tagName.equals("param") ){
					sdata.setParameter(
						etmp.getAttribute("key"),
						etmp.getAttribute("value")
					);
				}
			}
		}
		return sdata;
	}

	public static void main(String[] args)
	throws IOException {
		ScoreDataList list;

		ScoreListXmlExporter exporter = new ScoreListXmlExporter();
		exporter.load(new File("data.xml"));
		list = exporter.getScoreDataList();

		if( list == null ){
			System.out.println("失敗！");
			return;
		}

		for(int i=0; i<list.getDataCount(); i++){
			ScoreData data = list.get(i);
			System.out.print("[" + data.getId() + "]");
			System.out.print(" " + data.getTitle() + " (" + data.getIndex() + ")");
			System.out.println("{author: " + data.getParameter("author") + "}");
		}
	}
}
