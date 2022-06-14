package com.site.clearing.simulation.service;

/**
 * SimulationService interface
 * 
 * @author Vanaja
 *
 */
public interface SimulationService {

	void readSiteMap();

	void displaySiteMap();

	void executeSimulator();
	
	ExpenseReportService getExpenseReportService();

	void printExpenseReport(char[][] clearedSiteMap);

}
