package jp.tkch.sdtool.exporter;

import org.w3c.dom.Element;

import jp.tkch.htmlbuilder.css.CSSBlock;
import jp.tkch.htmlbuilder.css.CSSBlockList;
import jp.tkch.htmlbuilder.css.CSSProperty;
import jp.tkch.sdtool.model.ScoreDataList;
import jp.tkch.sdtool.model.ScoreDataListContainer;

public class ScoreListContainerHtmlExporter extends ScoreListHtmlExporter {
	private ScoreDataListContainer container;


	public ScoreListContainerHtmlExporter(){
		container = null;
	}

	public ScoreListContainerHtmlExporter(ScoreDataListContainer c0){
		this();
		setScoreDataListContainer(c0);
	}

	public void setScoreDataList(ScoreDataList l0){}
	public ScoreDataList getScoreDataList(){return null;}
	public void setLabel(String l0){}
	public String getLabel(){return null;}

	public void setScoreDataListContainer(ScoreDataListContainer c0){
		container = c0;
	}

	public ScoreDataListContainer getScoreDataListContainer(){
		return container;
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
		cssList.add(new CSSBlock(".page-break")
			.addProperty(new CSSProperty("page-break-after", "always")));

		Element eStyle = getDocument().createElement("style");
		eStyle.setTextContent(cssList.toString());

		return eStyle;
	}

	public Element createBody(){
		ScoreListTableHtmlBuilder tableBuilder= new ScoreListTableHtmlBuilder(getDocument());
		tableBuilder.setWidthConfig(ScoreListTableHtmlBuilder.DEFAULT_WIDTH);

		Element eBody = getDocument().createElement("body");
		Element eMark = getDocument().createElement("p");
		eMark.setTextContent("楽譜データベース");
		Element eTitle = getDocument().createElement("h1");
		eTitle.setTextContent("楽譜一覧");
		eBody.appendChild(eMark);
		eBody.appendChild(eTitle);

		for(int i=0; i<container.getListCount(); i++){
			tableBuilder.setScoreDataList(container.getList(i));
			tableBuilder.setLabel(container.getLabel(i));
			Element eTable = tableBuilder.buildScoreListTable();
			eBody.appendChild(eTable);

			if( i != (container.getListCount() - 1) ){
				Element eSpan = getDocument().createElement("span");
				eSpan.setAttribute("class", "page-break");
				eBody.appendChild(eSpan);
			}
		}

		return eBody;
	}

	public boolean build(){
		if( container != null && container.getListCount() > 0 ){
			super.setScoreDataList(container.getList(0));
			return super.build();
		}else{
			return false;
		}
	}
}
