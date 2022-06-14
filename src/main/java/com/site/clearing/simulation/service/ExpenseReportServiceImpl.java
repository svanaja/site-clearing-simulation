package com.site.clearing.simulation.service;

import static com.site.clearing.simulation.util.Constants.COMMUNICATION_OVERHEAD;
import static com.site.clearing.simulation.util.Constants.DESTRUCT_PROTECTED_TREES;
import static com.site.clearing.simulation.util.Constants.FUEL_USAGE;
import static com.site.clearing.simulation.util.Constants.REPAIRING_PAINT_DAMAGE;
import static com.site.clearing.simulation.util.Constants.UNCLEARED_SQUARE;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.site.clearing.simulation.repository.SimulationRepository;
import com.site.clearing.simulation.util.Constants;;

/**
 * This class implements ExpenseReportService interface
 * which takes care of generating itemized expense report
 * @author vanaja
 *
 */
@Service
public class ExpenseReportServiceImpl implements ExpenseReportService{

	private Map<Character, Integer> clearedSiteMap = new HashMap<>();
	
	private Map<Character, Integer> unClearedSiteMap = new HashMap<>();
	
	private int totalExpense = 0;

	SimulationRepository repository = SimulationRepository.getRepository();


	/**
	 * This method calculates the fuel usage for clearing the site by bulldozer
	 */
	public Integer calculateFuelUsage() {
		Map<Character, Integer> fuelLookup = repository.getFuelLookup();
		Integer fuelUsage = 0;

		for (Character landType : clearedSiteMap.keySet()) {
			if (landType != 'x')
				fuelUsage += (clearedSiteMap.get(landType) * fuelLookup.get(landType));
		}

		return fuelUsage;
	}

	/**
	 * This method calculates the uncleared square after clearing the site by simulator
	 */
	public Integer calculateUnclearedSquares() {
		Integer totalUnclearedSite = 0;

		for (Character landType : unClearedSiteMap.keySet()) {
			if (Constants.PROTECTED_TREE != landType && landType != 'x')
				totalUnclearedSite += unClearedSiteMap.get(landType);
		}

		return totalUnclearedSite;
	}

	/** 
	 * This method calculates the number of protected tree destructed by bulldozer
	 */
	public Integer calculateDestructedTrees() {
		Integer protectedTrees = 0;
		for (Character landType : clearedSiteMap.keySet()) {
			if (Constants.PROTECTED_TREE == landType) {
				protectedTrees += clearedSiteMap.get(landType);
			}
		}
		return protectedTrees;
	}

	/**
	 * This method calculates the incurred cost for the provided item and quantity
	 * @param item
	 * @param quantity
	 * @return cost
	 */
	public Integer calculateIncurredCost(String item, Map<String, Integer> quantity) {
		Integer rate = repository.getOperationCostLookup().get(item);
		Integer cost = rate * quantity.get(item);
		totalExpense += cost;
		return cost;
	}

	/**
	 * This method prints the itemized expense report on the console
	 * @param siteMapArray
	 * @param clearedSiteMapArray
	 * @param commandCount
	 * @param paintScratch
	 *
	 */
	public void printItemizedExpenseReport(char[][] siteMapArray, char[][] clearedSiteMapArray, int commandCount,
			int paintScratch) {
		loadClearedSiteMap(siteMapArray, clearedSiteMapArray);
		Map<String, Integer> quantity = getItemizedQuantity(commandCount, paintScratch);

		System.out.println("\nThe costs for this land clearing operation were:");
		System.out.println();
		System.out.println("Item                               Quantity    Cost");
		System.out.printf("communication overhead                  %3d     %3d\n", 
				quantity.get(COMMUNICATION_OVERHEAD), calculateIncurredCost(COMMUNICATION_OVERHEAD, quantity));
		System.out.printf("fuel usage                              %3d     %3d\n", 
				quantity.get(Constants.FUEL_USAGE), calculateIncurredCost(FUEL_USAGE, quantity));
		System.out.printf("uncleared squares                       %3d     %3d\n",
				quantity.get(Constants.UNCLEARED_SQUARE), calculateIncurredCost(UNCLEARED_SQUARE, quantity));
		System.out.printf("destruction of protected tree           %3d     %3d\n",
				quantity.get(Constants.DESTRUCT_PROTECTED_TREES), calculateIncurredCost(DESTRUCT_PROTECTED_TREES, quantity));
		System.out.printf("paint damage to bulldozer               %3d     %3d\n",
				quantity.get(Constants.REPAIRING_PAINT_DAMAGE), calculateIncurredCost(REPAIRING_PAINT_DAMAGE, quantity));
		System.out.println("----                                              ");
		System.out.printf("Total                                           %3d\n", totalExpense);
		System.out.println();
	}
	
	/**
	 * This method to store the item and quantity for calculating itemized expense report
	 * @param siteMapArray
	 * @param clearedSiteMapArray
	 * @param commandCount
	 * @param paintScratch
	 * @return itemizedQuantity map
	 */
	public Map<String, Integer> getItemizedQuantity(int commandCount, int paintScratch) {
		Map<String, Integer> itemizedQuantity = new Hashtable<String, Integer>();

		itemizedQuantity.put(Constants.COMMUNICATION_OVERHEAD, commandCount);
		itemizedQuantity.put(Constants.FUEL_USAGE, calculateFuelUsage());
		itemizedQuantity.put(Constants.UNCLEARED_SQUARE, calculateUnclearedSquares());
		itemizedQuantity.put(Constants.DESTRUCT_PROTECTED_TREES, calculateDestructedTrees());
		itemizedQuantity.put(Constants.REPAIRING_PAINT_DAMAGE, paintScratch);

		return itemizedQuantity;
	}

	
	/**
	 * This method load the cleared and unclear site map array to map
	 * @param siteMapArray
	 * @param clearedSiteMapArray
	 */
	private void loadClearedSiteMap(char[][] siteMapArray, char[][] clearedSiteMapArray) {
		for (int i = 0; i < siteMapArray.length; i++) {
			for (int j = 0; j < siteMapArray[i].length; j++) {

				char grid = siteMapArray[i][j];
				if (unClearedSiteMap.containsKey(grid)) {
					unClearedSiteMap.put(grid, unClearedSiteMap.get(grid) + 1);
				} else {
					unClearedSiteMap.put(grid, 1);
				}

				grid = clearedSiteMapArray[i][j];
				if (clearedSiteMap.containsKey(grid)) {
					clearedSiteMap.put(grid, clearedSiteMap.get(grid) + 1);
				} else {
					clearedSiteMap.put(grid, 1);
				}
			}
		}
	}

	public Integer getTotalExpense() {
		return totalExpense;
	}
	
	public Map<Character, Integer> getClearedSiteMap() {
		return clearedSiteMap;
	}

	public Map<Character, Integer> getUnClearedSiteMap() {
		return unClearedSiteMap;
	}
}
