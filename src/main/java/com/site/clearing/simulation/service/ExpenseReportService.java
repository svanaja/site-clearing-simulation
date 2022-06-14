package com.site.clearing.simulation.service;

import java.util.Map;

/**
 * ExpenseReportService interface
 * 
 * @author vanaja
 *
 */

public interface ExpenseReportService {

	Integer calculateFuelUsage();

	Integer calculateUnclearedSquares();

	Integer calculateDestructedTrees();

	Integer calculateIncurredCost(String item, Map<String, Integer> quantity);
	
	Map<String, Integer> getItemizedQuantity(int commandCount, int paintScratch);

	void printItemizedExpenseReport(char[][] siteMapArray, char[][] clearedSiteMapArray, int commandCount,
			int paintScratch);
	
	Integer getTotalExpense();
	
	public Map<Character, Integer> getClearedSiteMap();
	
	public Map<Character, Integer> getUnClearedSiteMap();

}
