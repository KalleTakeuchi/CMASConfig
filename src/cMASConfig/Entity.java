package cMASConfig;

public abstract class Entity {

	public String ID;
	public String InstanceID;
	public String Name;
	public String Description;
	public EntityType Type;
	
	protected void InitializeEntity(String id, String name, String description, EntityType type) {
		this.ID = id;
		this.Name = name;
		this.Description = description;
		this.Type = type;
	}
}
