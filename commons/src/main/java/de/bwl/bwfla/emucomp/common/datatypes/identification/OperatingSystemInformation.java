/*
 * This file is part of the Emulation-as-a-Service framework.
 *
 * The Emulation-as-a-Service framework is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * The Emulation-as-a-Service framework is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Emulation-as-a-Software framework.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package de.bwl.bwfla.emucomp.common.datatypes.identification;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class OperatingSystemInformation
{

	@XmlElement
	private String id;

	@XmlElement
	private String label;

	@XmlElement
	private List<String> puids;

	@XmlElement
	private List<String> extensions;

	@XmlElement
	private String qid;

	@XmlElement
	private boolean kvmAvailable;

	@XmlElement
	private String disk;

	@XmlElement
	private String template;

	@XmlElement
	private boolean runtime;

	@XmlElement
	private TemplateParameters templateParameters;

	@XmlElement
	private UiOptions uiOptions;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public List<String> getPuids()
	{
		return puids;
	}

	public void setPuids(List<String> puids)
	{
		this.puids = puids;
	}

	public List<String> getExtensions()
	{
		return extensions;
	}

	public void setExtensions(List<String> extensions)
	{
		this.extensions = extensions;
	}

	public String getQid()
	{
		return qid;
	}

	public void setQid(String qid)
	{
		this.qid = qid;
	}

	public boolean isKvmAvailable()
	{
		return kvmAvailable;
	}

	public void setKvmAvailable(boolean kvmAvailable)
	{
		this.kvmAvailable = kvmAvailable;
	}

	public String getDisk()
	{
		return disk;
	}

	public void setDisk(String disk)
	{
		this.disk = disk;
	}

	public String getTemplate()
	{
		return template;
	}

	public void setTemplate(String template)
	{
		this.template = template;
	}

	public TemplateParameters getTemplateParameters()
	{
		return templateParameters;
	}

	public void setTemplateParameters(TemplateParameters templateParameters)
	{
		this.templateParameters = templateParameters;
	}

	public UiOptions getUiOptions()
	{
		return uiOptions;
	}

	public void setUiOptions(UiOptions uiOptions)
	{
		this.uiOptions = uiOptions;
	}

	public boolean isRuntime()
	{
		return runtime;
	}

	public void setRuntime(boolean runtime)
	{
		this.runtime = runtime;
	}

	public static class UiOptions
	{
		@XmlElement
		private boolean useXpra;


		@XmlElement
		private String xpraEncoding;

		@XmlElement
		private boolean useWebRTC;

		public UiOptions()
		{
		}

		public boolean isUseXpra()
		{
			return useXpra;
		}

		public void setUseXpra(boolean useXpra)
		{
			this.useXpra = useXpra;
		}

		public String getXpraEncoding()
		{
			return xpraEncoding;
		}

		public void setXpraEncoding(String xpraEncoding)
		{
			this.xpraEncoding = xpraEncoding;
		}

		public boolean isUseWebRTC()
		{
			return useWebRTC;
		}

		public void setUseWebRTC(boolean useWebRTC)
		{
			this.useWebRTC = useWebRTC;
		}
	}


	public static class TemplateParameters
	{

		@XmlElement
		private String vga;

		@XmlElement
		private String cpu;

		@XmlElement
		private String net;

		@XmlElement
		private String audio;

		@XmlElement
		private String memory;

		@XmlElement
		private String pointer;

		@XmlElement
		private boolean kvmEnabled;

		@XmlElement
		private String rom;

		@XmlElement
		private List<String> models;

		@XmlElement
		private String chosenModel;

		@XmlElement
		private String args;

		@XmlElement
		private String url;

		@XmlElement
		private boolean fullscreen;

		@XmlElement
		private String size;

		public TemplateParameters()
		{
		}

		public String getVga()
		{
			return vga;
		}

		public void setVga(String vga)
		{
			this.vga = vga;
		}

		public String getCpu()
		{
			return cpu;
		}

		public void setCpu(String cpu)
		{
			this.cpu = cpu;
		}

		public String getNet()
		{
			return net;
		}

		public void setNet(String net)
		{
			this.net = net;
		}

		public String getAudio()
		{
			return audio;
		}

		public void setAudio(String audio)
		{
			this.audio = audio;
		}

		public String getMemory()
		{
			return memory;
		}

		public void setMemory(String memory)
		{
			this.memory = memory;
		}

		public String getPointer()
		{
			return pointer;
		}

		public void setPointer(String pointer)
		{
			this.pointer = pointer;
		}

		public boolean isKvmEnabled()
		{
			return kvmEnabled;
		}

		public void setKvmEnabled(boolean kvmEnabled)
		{
			this.kvmEnabled = kvmEnabled;
		}

		public String getRom()
		{
			return rom;
		}

		public void setRom(String rom)
		{
			this.rom = rom;
		}

		public List<String> getModels()
		{
			return models;
		}

		public void setModels(List<String> models)
		{
			this.models = models;
		}

		public String getChosenModel()
		{
			return chosenModel;
		}

		public void setChosenModel(String chosenModel)
		{
			this.chosenModel = chosenModel;
		}

		public String getArgs()
		{
			return args;
		}

		public void setArgs(String args)
		{
			this.args = args;
		}

		public String getUrl()
		{
			return url;
		}

		public void setUrl(String url)
		{
			this.url = url;
		}

		public boolean isFullscreen()
		{
			return fullscreen;
		}

		public void setFullscreen(boolean fullscreen)
		{
			this.fullscreen = fullscreen;
		}

		public String getSize()
		{
			return size;
		}

		public void setSize(String size)
		{
			this.size = size;
		}
	}

}
