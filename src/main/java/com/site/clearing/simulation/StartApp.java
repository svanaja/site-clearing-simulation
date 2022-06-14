package com.site.clearing.simulation;

import java.io.File;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import com.site.clearing.simulation.service.SimulationService;
import com.site.clearing.simulation.service.SimulationServiceImpl;

/**
 * This is an entry point of Site-Clearing-Simulation Application
 * 
 * @author Vanaja
 *
 */
@SpringBootApplication
public class StartApp implements ApplicationRunner {

	/**
	 * This is main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(StartApp.class, args);
	}

	@Lazy(true)
	@Bean(name = "service")
	@Scope(value = "prototype")
	SimulationService service(String fileName) {
		return new SimulationServiceImpl(fileName);
	}

	@Autowired
	private ApplicationContext context;

	/**
	 * 
	 * Overriding default implementation of run() method
	 *
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		String[] arguments = args.getSourceArgs();
		System.out.println("Welcome to the Aconex site clearing simulator. This is a map of " + "the site:\r\n");
		if (0 < arguments.length) {
			SimulationService service = (SimulationService) context.getBean("service", arguments[0]);
			service.executeSimulator();
		} else {
			System.err.println("Invalid arguments count:" + arguments.length);
			System.exit(0);
		}
	}
}
