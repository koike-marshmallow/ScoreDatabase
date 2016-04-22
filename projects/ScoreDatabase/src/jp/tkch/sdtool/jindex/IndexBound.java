package jp.tkch.sdtool.jindex;

public class IndexBound {
	private char start;
	private char end;

	public IndexBound(){
		start = 0;
		end = 0;
	}

	public IndexBound(char s0, char e0){
		start = s0;
		end = e0;
	}

	public boolean isMatch(char c){
		return (c >= start && c <= end);
	}

	public char getStart() {
		return start;
	}

	public void setStart(char start) {
		this.start = start;
	}

	public char getEnd() {
		return end;
	}

	public void setEnd(char end) {
		this.end = end;
	}
}
