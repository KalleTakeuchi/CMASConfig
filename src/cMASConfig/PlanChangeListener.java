package cMASConfig;

public interface PlanChangeListener {
	void planIDChanged(String oldID, Plan plan);
	void planNameChanged(Plan plan);
	void executionChanged();
}
