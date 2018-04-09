package test;

import io.JavaSourceCollector;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipException;

import org.junit.Test;

public class JavaSourceCollectorTest {
	
	JavaSourceCollector jsc;
	String TestDir = System.getProperty("user.dir") + "\\BASEDIR";
	
	@Test (expected = IllegalStateException.class)
	public void invalidPathTest() throws Exception{
		jsc = new JavaSourceCollector("gibberish");
		jsc.getSource();
	}
	
	@Test (expected = ZipException.class)
	public void blankJarFileTest() throws Exception{
		jsc = new JavaSourceCollector(TestDir + "\\BlankFileTest.jar");
		jsc.getSource();
	}
	
	@Test (expected = IllegalStateException.class)
	public void invalidFileTest() throws Exception{
		jsc = new JavaSourceCollector(TestDir + "\\InvalidFileTest.bin");
		jsc.getSource();
	}
	
	@Test
	public void normalFolderTest() throws Exception{
		jsc = new JavaSourceCollector(TestDir + "\\OneLayerFolder");
		List<String> testSource = jsc.getSource();
		List<String> testCase = new ArrayList<String>();
		testCase.add("TEST1");
		testCase.add("TEST2");
		assertTrue(testCase.containsAll(testSource));
		assertTrue(testSource.containsAll(testCase));
	}
	
	@Test
	public void recursiveFolderTest() throws Exception{
		jsc = new JavaSourceCollector(TestDir + "\\MultipleLayersFolder");
		List<String> testSource = jsc.getSource();
		List<String> testCase = new ArrayList<String>();
		testCase.add("TESTA");
		testCase.add("TESTB");
		testCase.add("TESTC");
		testCase.add("TESTD");
		testCase.add("TESTE");
		assertTrue(testCase.containsAll(testSource));
		assertTrue(testSource.containsAll(testCase));
	}
	
	@Test
	public void normalJARFileTest() throws Exception{
		jsc = new JavaSourceCollector(TestDir + "\\NormalJarFile.jar");
		List<String> testSource = jsc.getSource();
		List<String> testCase = new ArrayList<String>();
		testCase.add("TESTA");
		testCase.add("TESTB");
		testCase.add("TESTC");
		testCase.add("TESTD");
		testCase.add("TESTE");
		assertTrue(testCase.containsAll(testSource));
		assertTrue(testSource.containsAll(testCase));
	}
}