package de.bwl.bwfla.emucomp.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.bwl.bwfla.emucomp.common.utils.jaxb.JaxbType;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;

@JsonIgnoreProperties("urlPrefix")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "imageArchiveBinding", namespace = "http://bwfla.bwl.de/common/datatypes", propOrder = {
		"imageId", "type", "fileSystemType", "backendName" })
@XmlRootElement(namespace = "http://bwfla.bwl.de/common/datatypes")
public class ImageArchiveBinding extends Binding
{
	@XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = true)
	protected String backendName;

	@XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = true)
	protected String imageId;

	@XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = true)
	protected String type;

	@XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false)
	protected String fileSystemType;

	@XmlTransient
	protected Kind kind = null;

	public enum Kind
	{
		CHECKPOINT,
		EMULATOR,
		IMAGE,
		ROM,
	}

	public ImageArchiveBinding()
	{
		backendName = null;
		imageId = null;
		type = null;
		fileSystemType = null;
	}

	public ImageArchiveBinding(String backend, String imageId, String type)
	{
		this(backend, imageId, type,null);
	}

	public ImageArchiveBinding(String backend, String imageId, String type, String fileSystemType)
	{
		this.backendName = backend;
		this.imageId = imageId;
		this.type = type;
		this.fileSystemType = fileSystemType;
	}

	public void copy(ImageArchiveBinding b)
	{
		this.backendName = b.backendName;
		this.imageId = b.imageId;
		this.type = b.type;
		this.fileSystemType = b.fileSystemType;
	}

	/** Replaces this with all non-null fields from other */
	public void update(ImageArchiveBinding other)
	{
		// id will remain identical or needs to be set explicitly
		// this.id = other.imageId;

		if (other.backendName != null)
			this.backendName = other.backendName;

		if (other.imageId != null)
			this.imageId = other.imageId;

		if (other.type != null)
			this.type = other.type;

		if (other.fileSystemType != null)
			this.fileSystemType = other.fileSystemType;
	}

	@Override
	public void setId(String newid)
	{
		super.setId(newid);
		this.updateKind();
	}

	private void updateKind()
	{
		if (id.equals("emucon-rootfs"))
			kind = Kind.EMULATOR;
		else if (id.startsWith("rom-"))
			kind = Kind.ROM;
		else if (id.equals("checkpoint"))
			kind = Kind.CHECKPOINT;
		else kind = Kind.IMAGE;
	}

	public Kind getKind()
	{
		if (kind == null)
			this.updateKind();

		return kind;
	}

	public String getBackendName() {
		return backendName;
	}

	public void setBackendName(String name) {
		this.backendName = name;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFileSystemType() {
		return fileSystemType;
	}

	public void setFileSystemType(String type) {
		this.fileSystemType = type;
	}

	public static ImageArchiveBinding fromValue(String value) throws JAXBException
	{
		return JaxbType.fromValue(value, ImageArchiveBinding.class);
	}
}
