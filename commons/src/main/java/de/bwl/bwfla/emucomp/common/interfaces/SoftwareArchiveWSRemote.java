package de.bwl.bwfla.emucomp.common.interfaces;

import de.bwl.bwfla.emucomp.common.datatypes.SoftwareDescription;
import de.bwl.bwfla.emucomp.common.datatypes.SoftwarePackage;
import de.bwl.bwfla.emucomp.common.exceptions.BWFLAException;

import javax.activation.DataHandler;


public interface SoftwareArchiveWSRemote
{
	public boolean hasSoftwarePackage(String id);

	public boolean addSoftwarePackage(SoftwarePackage software);
	public boolean changeSoftwareLabel(String objectId, String newLabel);
	
	public int getNumSoftwareSeatsById(String id);
	public int getNumSoftwareSeatsForTenant(String id, String tenant) throws BWFLAException;
	public void setNumSoftwareSeatsForTenant(String id, String tenant, int seats) throws BWFLAException;
	public void resetNumSoftwareSeatsForTenant(String id, String tenant) throws BWFLAException;
	public void resetAllSoftwareSeatsForTenant(String tenant) throws BWFLAException;

	public SoftwarePackage getSoftwarePackageById(String id);

	public DataHandler getSoftwarePackages();

	public DataHandler getSoftwarePackageIds();

	public SoftwareDescription getSoftwareDescriptionById(String id);
	
	public DataHandler getSoftwareDescriptions();
	
	public String getName();

	public void delete(String id);

	public void sync() throws BWFLAException;
}
