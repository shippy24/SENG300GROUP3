//iter3
package main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import io.JavaSourceCollector;

public class Foo {
	/**
	 * Points to a base directory of a machine</br>
	 * @author SeungBin, Yim
	 */
	private static String BASEDIR = "/Users/shayn/Desktop/Projects/deeplearning4j-master";
	/**
	 * Constructor, prints out all declarations and references from sources in BASEDIR </br>	 * 
	 * @author SeungBin, Yim
	 */
	
	static ArrayList<Integer> al = new ArrayList<Integer>();
	static ArrayList<Integer> bd = new ArrayList<Integer>();
	static ArrayList<Integer> cz = new ArrayList<Integer>();
	static ArrayList<Integer> it = new ArrayList<Integer>();
	static ArrayList<Integer> pr = new ArrayList<Integer>();
	static ArrayList<Integer> at = new ArrayList<Integer>();
	static ArrayList<Integer> im = new ArrayList<Integer>();	
	static ArrayList<Integer> an = new ArrayList<Integer>();	
	static ArrayList<Integer> la = new ArrayList<Integer>();	
	static ArrayList<Integer> ao = new ArrayList<Integer>();	
	
	
	public Foo() {
		Map<String, Integer[]> finalMap = new HashMap();
		File directory = new File(BASEDIR);
		JavaSourceCollector jsc = new JavaSourceCollector(BASEDIR);
		List<String> sourceList = null;
		try {
			sourceList = jsc.getSource();
		}catch (Exception e) { e.printStackTrace(); }
		
		for(String source : sourceList) {
			if(source.trim().startsWith("//")) continue;
			Map<String, Integer[]> SourceMap = visit(parse(source));
			for(String key : SourceMap.keySet())
				if(finalMap.get(key) == null)
					finalMap.put(key,  SourceMap.get(key));
				else {
					Integer[] value = finalMap.get(key);
					value[0] += SourceMap.get(key)[0];
					value[1] += SourceMap.get(key)[1];
					finalMap.put(key, value);
				}
		}	
		
		if(finalMap == null) return;
		//Sort keys
		SortedSet<String> keyset = new TreeSet();
		keyset.addAll(finalMap.keySet());
		
		//for(String truekey: keyset) {
		//	System.out.printf(
		//			"%-30s. Declarations found: %s;\treferences found: %s.\n",
		//			truekey, finalMap.get(truekey)[0], finalMap.get(truekey)[1] );
		//}
	
		
	}
	/**
	 * Parses source String into AST Tree
	 * @param sourceCode
	 * 	Source String to be parsed by ASTParser
	 * @return
	 * 	Root node of a parsed AST
	 * @author SeungBin, Yim
	 */
	public ASTNode parse(String sourceCode) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(sourceCode.toCharArray());
		
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		
		parser.setUnitName("");
		
		parser.setEnvironment(null,
				new String[] {BASEDIR}, new String[]{"UTF-8"}, true);
		
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5); //or newer version
		parser.setCompilerOptions(options);
		
		return parser.createAST(null);
	}
	/**
	 * Visits AST tree, counting declarations and references
	 * @param node
	 * Root node of a AST
	 * @author SeungBin, Yim
	 */
	public Map<String, Integer[]> visit(ASTNode node) {
		Visitor vis = new Visitor();
		CompilationUnit cu = (CompilationUnit)node;
		cu.accept(vis);
		al.add(vis.nestedCount);
		bd.add(vis.localCount);
		cz.add(vis.anonymousCount);
		it.add(vis.otherInterCount);
		pr.add(vis.primiCount);
		at.add(vis.annotCount);
		an.add(vis.nestedAnnotCount);
		la.add(vis.localAnnotCount);
		ao.add(vis.otherAnnotCount);
		
		return vis.getMap();	
	}
	
	// USED TO DISPLAY COUNT ADDED 
	public static void displayCount() {
		int x = 0; // nested
		int y = 0; // local
		int z = 0; // anonymous
		int q = 0; // otherinterfaces
		int p = 0; // ints
		int a = 0; // annotations
		int i = 0; // imports
		int c = 0; // nestedannot
		int d = 0; // local annot
		int e = 0; // other annot
		
		for (int object: al) {
			x = x + object;
		}
		
		for (int object: bd) {
			y = y + object;
		}
		
		for (int object: cz) {
			z = z + object;
		}
		
		for (int object: it) {
			q = q + object;
		}
		
		for (int object: pr) {
			p = p + object;
		}
		
		for (int object: at) {
			a = a + object;
		}
		
		for (int object: im) {
			i = i  + object;
		}
		

		for (int object: an) {
			c = c  + object;
		}
		
		for (int object: la) {
			d = d  + object;
		}
		
		for (int object: ao) {
			e = e  + object;
		}
		
		System.out.println("Nested type count: " + x);
		System.out.println("Local type count: " + y);
		System.out.println("Anonymous type count: " + z);
		System.out.println("Other Interface and Class declarations: " + q); // non nested, non local, non anon
		System.out.println("Primitve type count: " + p);
		System.out.println("Marker Annotation count: " + a);
		System.out.println("Import declarations count: " + i);
		System.out.println("Nested annotation count:" + c);
		System.out.println("Local annotation count: " + d);
		System.out.println("Other annotation count: " + e);
	}
	
	
	//Program Entry Point
	public static void main(String[] args) {
	//	BASEDIR = System.getProperty("user.dir")+"\\BASEDIR";
		new Foo();
		displayCount();
	}
}