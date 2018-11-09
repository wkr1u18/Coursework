import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CourseworkTest {
	//OutputCapturer class taken for Dr. Packer's code for COMP1202 labs
	class OutputCapturer {
		private PrintStream origOut;

		private ByteArrayOutputStream outputStream;

		public void start()
		{
			this.origOut = System.out;
			this.outputStream = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(this.outputStream);
			System.setOut(ps);
		}

		public String getOutput() {
			System.out.flush();
			return this.outputStream.toString().replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
		}
		public void stop() {
			System.setOut(this.origOut);
		}
	}
	
	@Test
	@DisplayName("Test Meter is constructed")
	void MeterConstructorTest() {
		//Creates and initialises Meter object.
		Meter testMeter = new Meter("Water", (double) 2.5, (float) 0);
		assertNotNull(testMeter, "checks Meter is not null");	
	}
	
	@Test
	@DisplayName("Tests Meter consume() and report() method")
	void MeterValuesTest() {
		Meter testMeter = new Meter("Water", (double) 2.5, (float) 0);
		testMeter.consumeUnits((float) 10.5);
		OutputCapturer outputHarness = new OutputCapturer();
		outputHarness.start();
		testMeter.report();
		outputHarness.stop();		
		String output = outputHarness.getOutput();
		assertTrue(output.contains((String) new Double(10.5*2.5).toString()), "Calculates the price the right way");
		outputHarness.start();
		testMeter.report();
		outputHarness.stop();
		output = outputHarness.getOutput();
		assertTrue(output.contains((String) new Double(0).toString()), "Resets the meter the right way");
		
	}	

}
