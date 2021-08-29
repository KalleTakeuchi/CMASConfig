package cMASConfig;

public interface SkillChangeListener {
	void skillNameChanged(Skill skill);
	void skillIDChanged(String oldID, Skill skill);
	void skillRemoved(Skill skill);
	void executionChanged();
}
