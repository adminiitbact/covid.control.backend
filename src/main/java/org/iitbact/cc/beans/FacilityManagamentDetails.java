package org.iitbact.cc.beans;

public interface FacilityManagamentDetails {
	
	public int facilityId();
	
	public int getToltalBeds();

	public int getCovidIcuBeds();

	public int getTotalIcuBeds();

	public int getTotalVentilators();

	public int getTotalIcuBedsOccupied();

	public int getVentilatorsAllocatedCovid();

	public int getMildSuspectedTotal();

	public int getMildSuspectedOccupied();

	public int getMildConfirmedTotal();

	public int getMildConfirmedOccupied();

	public int getModerateSuspectedTotal();

	public int getModerateSuspectedOccupied();

	public int getModerateConfirmedTotal();

	public int getModerateConfirmedOccupied();

	public int getSevereSuspectedTotal();

	public int getSevereSuspectedOccupied();

	public int getSevereConfirmedTotal();

	public int getSevereConfirmedOccupied();

	public int getVentilatorsEarmarkedForCovid();

	public int getVentilatorInUseCovid();

	public int getOxygenCylinderAvailable();

	public int getWallOxygenSuppliedBeds();

	public int getCentralOxygenSuppliedBeds();

	public int getPulseOximeters();

	public int getInfusionPumps();

	public int getIndependentBeds();

}
