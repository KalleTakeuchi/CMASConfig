package cMASConfig;

public interface StatementFormListener {
	void moveDown(StatementForm sender);
	void moveUp(StatementForm sender);
	void deleteRequest(StatementForm sender);
	void formChanged();
	void repaintRequest();
}
