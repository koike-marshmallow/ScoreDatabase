package jp.tkch.sdtool.exporter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jp.tkch.sdtool.model.ScoreData;
import jp.tkch.sdtool.model.ScoreDataList;
import jp.tkch.sdtool.model.ScoreDataParameter;

public class ScoreListXmlExporter {
	private ScoreDataList list;
	private Document doc;

	public ScoreListXmlExporter(ScoreDataList l0){
		list = l0;
		doc = null;
	}

	public void build(){

	}

	public static Element scoreDataToElement(Document doc, ScoreData sdata){
		Element eScore = doc.createElement("score");
		eScore.setAttribute("id", sdata.getId());

		Element eTitle = doc.createElement("title");
		eTitle.setAttribute("value", sdata.getTitle());
		eScore.appendChild(eTitle);
		Element eIndex = doc.createElement("index");
		eTitle.setAttribute("value", sdata.getIndex());
		eScore.appendChild(eIndex);

		for(ScoreDataParameter param : sdata.getParameters()){
			Element eParamTmp = doc.createElement("param");
			eParamTmp.setAttribute("key", param.getKey());
			eParamTmp.setAttribute("value", param.getValue());
			eScore.appendChild(eParamTmp);
		}

		return eScore;
	}
}
