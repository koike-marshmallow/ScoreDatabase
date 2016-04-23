package jp.tkch.sdtool.manager;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import jp.tkch.sdtool.exporter.ScoreListXmlExporter;
import jp.tkch.sdtool.model.ScoreData;
import jp.tkch.sdtool.model.ScoreDataFinder;
import jp.tkch.sdtool.model.ScoreDataList;

public class DualScoreDataList {
	private Comparator<ScoreData> comparator;
	private ScoreDataList master;
	private ScoreDataList current;

	private File dbFile;

	public DualScoreDataList(){
		comparator = null;
		master = createListInstance();
		resetCurrent();
	}

	public DualScoreDataList(Comparator<ScoreData> c0){
		comparator = c0;
		master = createListInstance();
		resetCurrent();
	}

	ScoreDataList createListInstance(){
		if( comparator != null ){
			return new ScoreDataList(comparator);
		}else{
			return new ScoreDataList();
		}
	}

	ScoreDataList createListInstance(ScoreDataList list){
		return createListInstance(list, true);
	}

	ScoreDataList createListInstance(ScoreDataList list, boolean refresh){
		ScoreDataList newList;
		if( refresh ){
			newList = createListInstance();
			newList.add(list);
		}else{
			newList = list;
			newList.setComparator(comparator);
		}
		return newList;
	}

	public ScoreDataList getMaster(){
		return master;
	}

	public void resetCurrent(){
		current = createListInstance(master);
	}

	public void setCurrent(ScoreDataList l0){
		current = createListInstance(l0);
	}

	public ScoreDataList setCurrent(ScoreDataFinder f0){
		current = createListInstance(master.createDataList(f0), false);
		return current;
	}

	public ScoreDataList getCurrent(){
		return current;
	}

	public void setDbFile(File f0){
		dbFile = f0;
	}

	public File getDbFile(){
		return dbFile;
	}

	public boolean loadDatabase(){
		if( dbFile == null ) return false;

		ScoreListXmlExporter exporter = new ScoreListXmlExporter();

		try {
			if( !exporter.load(dbFile) ){
				System.err.println("loadDatabase: import error");
				return false;
			}
		} catch (IOException e0) {
			System.err.println("loadDatabase: file error");
			return false;
		}

		master = createListInstance(exporter.getScoreDataList());
		return true;
	}

	public boolean saveDatabase(){
		if( dbFile == null ) return false;

		ScoreListXmlExporter exporter = new ScoreListXmlExporter(master);
		try{
			if( !exporter.save(dbFile) ){
				System.err.println("saveDatabase: export error");
				return false;
			}
		}catch(IOException e0){
			System.err.println("saveDatabase: file error");
			return false;
		}
		return true;
	}




}
