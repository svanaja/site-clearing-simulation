package com.site.clearing.simulation.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.site.clearing.simulation.util.Constants;

/**
 * This is a singleton Repository class
 * 
 * @author Vanaja
 *
 */
@Repository
public class SimulationRepository {

	private static final Map<Character, Integer> fuel_usage;

	private static final Map<String, Integer> operation_cost;

	private static SimulationRepository repository;
	
	
	static {
		Map<Character, Integer> fuelMap = new HashMap<Character, Integer>();
		fuelMap.put(Constants.PLAIN_LAND, 1);
		fuelMap.put(Constants.ROCK_LAND, 2);
		fuelMap.put(Constants.REMOVABLE_TREE, 2);
		fuelMap.put(Constants.PROTECTED_TREE, 2);
		fuel_usage = Collections.unmodifiableMap(fuelMap);

		Map<String, Integer> costMap = new Hashtable<String, Integer>();
		costMap.put(Constants.COMMUNICATION_OVERHEAD, 1);
		costMap.put(Constants.FUEL_USAGE, 1);
		costMap.put(Constants.UNCLEARED_SQUARE, 3);
		costMap.put(Constants.DESTRUCT_PROTECTED_TREES, 10);
		costMap.put(Constants.REPAIRING_PAINT_DAMAGE, 2);
		operation_cost = Collections.unmodifiableMap(costMap);

	}

	/**
	 * Private constructor to restrict instantiation outside class
	 */
	private SimulationRepository() {

	}

	/**
	 * This method is responsible for creating a single instance of Simulation
	 * Repository
	 * 
	 * @return SimulationRepository
	 */
	public static SimulationRepository getRepository() {

		if (repository == null) {
			synchronized (SimulationRepository.class) {
				if (repository == null) {
					repository = new SimulationRepository();
				}
			}
		}
		return repository;
	}

	/**
	 * The getter method of fuel_usage
	 * 
	 * @return 
	 */
	public Map<Character, Integer> getFuelLookup() {
		return fuel_usage;
	}

	/**
	 * The getter method of operation_cost
	 * 
	 * @return
	 */
	public Map<String, Integer> getOperationCostLookup() {
		return operation_cost;
	}
}
