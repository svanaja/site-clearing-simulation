package com.construction.site_clearing_simulation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import com.site.clearing.simulation.service.ExpenseReportService;
import com.site.clearing.simulation.service.SimulationService;
import com.site.clearing.simulation.service.SimulationServiceImpl;

import static com.site.clearing.simulation.util.Constants.COMMUNICATION_OVERHEAD;
import static com.site.clearing.simulation.util.Constants.FUEL_USAGE;
import static com.site.clearing.simulation.util.Constants.UNCLEARED_SQUARE;
import static com.site.clearing.simulation.util.Constants.DESTRUCT_PROTECTED_TREES;
import static com.site.clearing.simulation.util.Constants.REPAIRING_PAINT_DAMAGE;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AppTest {
	
	String fileName = "sitemap.txt";

	@Bean
	SimulationService simulationService() {
		return new SimulationServiceImpl(fileName);
	}

	@Test
	public void shouldAnswerWithTrue() {
		assertTrue(true);
	}

	@Test
	public void testTotalExpenseCost() {
		SimulationService service = simulationService();
		service.readSiteMap();
		ExpenseReportService reportService = service.getExpenseReportService();
		Map<Character, Integer> clearedSiteMap = reportService.getClearedSiteMap();
		clearedSiteMap.put('o', 9);
		clearedSiteMap.put('r', 3);
		clearedSiteMap.put('t', 2);
		clearedSiteMap.put('T', 0);
		
		Map<Character, Integer> unClearedSiteMap = reportService.getUnClearedSiteMap();
		unClearedSiteMap.put('o', 25);
		unClearedSiteMap.put('r', 9);
		unClearedSiteMap.put('t', 0);
		unClearedSiteMap.put('T', 2);
		
		List<String> commands = new ArrayList<String>(Arrays.asList("a 4","r","a 4","l","a 2","a 4","l"));
		Map<String, Integer> quantity = reportService.getItemizedQuantity(commands.size(), 1);

		assertEquals(reportService.calculateIncurredCost(COMMUNICATION_OVERHEAD, quantity), 7);
		assertEquals(reportService.calculateIncurredCost(FUEL_USAGE, quantity), 19);
		assertEquals(reportService.calculateIncurredCost(UNCLEARED_SQUARE, quantity), 102);
		assertEquals(reportService.calculateIncurredCost(DESTRUCT_PROTECTED_TREES, quantity), 0);
		assertEquals(reportService.calculateIncurredCost(REPAIRING_PAINT_DAMAGE, quantity), 2);
		
		assertEquals(reportService.getTotalExpense(), 130);
	}
	
}
