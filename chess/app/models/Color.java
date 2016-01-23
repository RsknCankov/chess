package models;

public enum Color {
	BLACK(7, 6, -1),
	WHITE(0, 1, 1);
	private int firstRow;
	private int secondRow;
	private int pawnDirection;
	Color(int firstRow, int secondRow, int pawnDirection){
		this.firstRow = firstRow;
		this.secondRow = secondRow;
		this.pawnDirection = pawnDirection;
	}
	public int getFirstRow(){
		return firstRow;
	}
	public int getSecondRow(){
		return secondRow;
	}
	public int getPawnDirection(){
		return pawnDirection;
	}
}
