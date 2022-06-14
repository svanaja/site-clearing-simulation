package com.site.clearing.simulation.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.site.clearing.simulation.repository.SimulationRepository;

/**
 * SimulationServiceImpl implements SimulationService interface
 * @author Vanaja
 *
 */
@Service
public class SimulationServiceImpl implements SimulationService {

	private String siteMapFile;
	private char[][] siteMapArray;

	private int[] x_y = { -1, -1 };
	private int[][] directionValue = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
	private int paint_damage = 0;

	private List<String> commands = new ArrayList<String>();

	@Autowired
	ExpenseReportService expenseReportService = new ExpenseReportServiceImpl();

	public SimulationServiceImpl(String inFile) {
		siteMapFile = inFile;
		readSiteMap();
	}
	
	public SimulationServiceImpl() {}

	public ExpenseReportService getExpenseReportService() {
		return expenseReportService;
	}
	
	/**
	 * This method to read the sitemap file provided in starting the simulator application
	 */
	@Override
	public void readSiteMap() {

		URL resource = getClass().getClassLoader().getResource(siteMapFile);
		try {
			File file = new File(resource.toURI());
			
			Scanner scanner = new Scanner(file);
			int rowCount = 0;
			List<String> arrayoflines = new ArrayList<String>();

			// to read the site Map file to find the row & column and create char array
			while (scanner.hasNextLine()) {
				arrayoflines.add(scanner.nextLine());
				rowCount++;
			}
			int columnCount = arrayoflines.get(0).toCharArray().length;

			siteMapArray = new char[rowCount][columnCount];

			// To store the site Map into char[][] array
			for (int i = 0; i < arrayoflines.size(); i++) {
				siteMapArray[i] = arrayoflines.get(i).toCharArray();
			}

			scanner.close();
		} catch (FileNotFoundException | URISyntaxException e) {
			System.err.print("Site Map file not found");
			e.printStackTrace();
		}
	}

	/**
	 * This method is responsible for displaying site map on console
	 * so that trainee instructs to move the bulldozer for clearing the site
	 */
	public void displaySiteMap() {
		printSiteMapArray();
		System.out.println("\nThe bulldozer is currently located at the Northern edge of the "
				+ "site, immediately to the West of the site, and facing East.\r\n");

	}

	/**
	 * This method to print the site map on console
	 */
	private void printSiteMapArray() {
		for (int i = 0; i < siteMapArray.length; i++) {
			for (int j = 0; j < siteMapArray[i].length; j++) {
				System.out.print(siteMapArray[i][j] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * This method execute the simulator and takes the command from console
	 * Accordingly the bulldozer move forward or change the direction 
	 * to clear the site
	 */
	@Override
	public void executeSimulator() {
		displaySiteMap();
		Scanner in = new Scanner(System.in);
		char[][] clearedSiteMap = new char[siteMapArray.length][siteMapArray[0].length];
		int directionIndex = 0;
		initializeClearedSiteMap(clearedSiteMap);

		while (true) {
			System.out.print("(l)eft, (r)ight, (a)dvance <n>, (q)uit: ");
			try {
				String cmd = in.nextLine();
				commands.add(cmd);
				String[] input = cmd.split(" ");
				switch (input[0]) {
				case "a":
					int moves = 0;
					if (input.length < 2)
						throw new Exception("Incorrect command ");
					moves = Integer.parseInt(input[1]);
					moveForward(clearedSiteMap, moves, directionIndex);
					break;

				case "r":
					directionIndex = directionIndex + 1;
					break;

				case "l":
					directionIndex = directionIndex - 1;
					break;

				case "q":
					System.out.println(
							"\nThe simulation has ended at your request. " + "These are the commands you issued:\n");
					printExpenseReport(clearedSiteMap);
					System.exit(0);
					break;

				default:
					System.out.println("Please enter right command ");
					in.next();
					break;

				}
			} catch (ArrayIndexOutOfBoundsException ex) {
				System.out.println("\nThe simulation has ended as bulldozer navigated beyond the boundaries. "
						+ "These are the commands you issued:\n");
				printExpenseReport(clearedSiteMap);
				System.exit(0);
			} catch (InterruptedException ex) {
				System.out.println("\nThe simulation has ended as bulldozer encountered protected tree. "
						+ "These are the commands you issued:\n");
				printExpenseReport(clearedSiteMap);
				System.exit(0);
			} catch (Exception ex) {
				System.err.println("Exception thrown : " + ex.getMessage());
				ex.printStackTrace();

			}
		}
	}

	/**
	 * This method to print the itemized Expense report to the console
	 * @param clearedSiteMap
	 */
	@Override
	public void printExpenseReport(char[][] clearedSiteMap) {
		System.out.println(getCommandIssued(commands));
		expenseReportService.printItemizedExpenseReport(siteMapArray, clearedSiteMap, commands.size() - 1, paint_damage);
		System.out.println("\nThank you for using the Aconex site clearing simulator. \n");
	}

	/**
	 * This method to initialize the clearedSiteMap array with 'x'
	 * @param clearedSiteMap
	 */
	private void initializeClearedSiteMap(char[][] clearedSiteMap) {
		for (int i = 0; i < clearedSiteMap.length; i++) {
			for (int j = 0; j < clearedSiteMap[i].length; j++) {
				clearedSiteMap[i][j] = 'x';
			}
		}
	}

	/**
	 * This method to move forward the bulldozer ahead for the specified moves
	 * 
	 * @param clearedSiteMap
	 * @param moves
	 * @param index
	 * @throws Exception
	 */
	private void moveForward(char[][] clearedSiteMap, int moves, int index) throws Exception {
		int x, y, move_x, move_y = 0;
		x = x_y[0];
		y = x_y[1];

		for (int i = 0; i < moves; i++) {
			move_x = directionValue[index][0];
			move_y = directionValue[index][1];
			x += move_x;
			y += move_y;
			if (x == -1)
				x = 0;
			if (y == -1)
				y = 0;
			clearedSiteMap[x][y] = siteMapArray[x][y];
			siteMapArray[x][y] = 'x';
		}

		x_y[0] = x;
		x_y[1] = y;
		//System.out.println("Reached the coordinates : " + x + "," + y);
		if (clearedSiteMap[x][y] == 't' || clearedSiteMap[x][y] == 'T') {
			paint_damage++;
			if (clearedSiteMap[x][y] == 'T')
				throw new InterruptedException("Destructed Protected Trees");
		}
	}

	/**
	 * This method to generate the list of commands issued by the trainee
	 * 
	 * @param list
	 * @return CommandIssues
	 */
	private String getCommandIssued(List<String> list) {
		StringBuffer command = new StringBuffer();
		for (String cmd : list) {
			switch (cmd.charAt(0)) {
			case 'a':
				command.append("advance " + cmd.split(" ")[1] + ", ");
				break;

			case 'r':
				command.append("turn right, ");
				break;

			case 'l':
				command.append("turn left, ");
				break;

			case 'q':
				command.append("quit");
				break;
			}
		}
		return command.toString();
	}

}
