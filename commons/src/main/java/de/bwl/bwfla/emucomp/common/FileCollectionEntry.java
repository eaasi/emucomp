package de.bwl.bwfla.emucomp.common;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "file", namespace="http://bwfla.bwl.de/common/datatypes")
public class FileCollectionEntry extends Binding implements Comparable<FileCollectionEntry>{
	
	@XmlElement(required = true, namespace="http://bwfla.bwl.de/common/datatypes")
	protected Drive.DriveType type;

	@XmlElement
	private ResourceType resourceType;

	@XmlElement(required = false, namespace="http://bwfla.bwl.de/common/datatypes")
	protected String order;

	@XmlElement(required = false, namespace="http://bwfla.bwl.de/common/datatypes")
	protected String label;

	@XmlElement(required = false, namespace="http://bwfla.bwl.de/common/datatypes", defaultValue = "false")
	private boolean isDefault = false;

	@XmlElement
	private String archive;

	@XmlElement
	private String objectId;

	public FileCollectionEntry()
	{
		this.url = null;
		this.type = null;
		this.id = null;
	}
	
	public FileCollectionEntry(String ref, Drive.DriveType type, String id) {
		this.url = ref;
		this.type = type;
		this.id = id;
	}

	public Drive.DriveType getType() {
		return type;
	}

	public void setType(Drive.DriveType type) {
		this.type = type;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean aDefault) {
		isDefault = aDefault;
	}

	@Override
	public int compareTo(FileCollectionEntry o) {
		return order.compareTo(o.order);
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public String getArchive() {
		return archive;
	}

	public void setArchive(String archive) {
		this.archive = archive;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String resolve(String exportUrlPrefix)
	{
		return exportUrlPrefix + "/" + this.getUrl();
	}
}
