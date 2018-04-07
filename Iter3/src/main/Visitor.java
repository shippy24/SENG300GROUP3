//iter3, now counts annotations
// now counts anon class types 
package main;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.*;

//Modified ASTVisitor
/**
 * AST Visitor for only visiting Declarations
 * @author SeungBin, Yim
 */
public class Visitor extends ASTVisitor{
	Map<String, Integer[]> map = new HashMap<String, Integer[]>();
	
	//public int[] counter;
	public int nestedCount = 0;
	public int localCount;
	public int anonymousCount;
	public int annotCount;
	public int primiCount;
	public int otherInterCount; // other interface declarations (non local, non nested, non anon)
	public int importDecCount; // not so important
	
	//public Integer otherInterCount;
	
	public Map<String, Integer[]> getMap(){
		return map;
	}
	
	//Visits when there is a primitive type (int, char, ...)
	@Override
	public boolean visit(PrimitiveType node) {
		if(!node.toString().equals("void")) {
			String key = node.resolveBinding().getQualifiedName();
			Integer[] count = map.get(key);
			primiCount ++ ; 
			if(count != null) 
				count[1]++;
			else
				count = new Integer[] {0,1};
			map.put(key, count);
		}
		return super.visit(node);
	}
	//Visits when there is a SimpleType type (non-Primitive types like java.lang.String)
	@Override
	public boolean visit(SimpleType node) {
		String key = node.resolveBinding().getQualifiedName();
		/*
		if(node.resolveBinding().getQualifiedName().isEmpty())
			key= node.resolveBinding().getName();
		else
			key = node.resolveBinding().getQualifiedName();
			*/
		//Not sure though
	//	annotCount ++; 
		if(node.getParent().toString().endsWith("[]"))
			key += "[]";
		//System.out.println(node.getParent());
		Integer[] count = map.get(key);
		if(count != null) 
			count[1]++;
		else
			count = new Integer[] {0,1};
		map.put(key, count);
		return super.visit(node);
	}
	
	//1-1. AnnotationType declaration
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		String key = node.resolveBinding().getQualifiedName();
		Integer[] count = map.get(key);
		annotCount ++;
		if(count != null) 
			count[0]++;
		else
			count = new Integer[] {1,0};
		map.put(key, count);
		return super.visit(node);
	}

	@Override
	public boolean visit(MarkerAnnotation node) {
		String key = node.resolveTypeBinding().getQualifiedName();
		Integer[] count = map.get(key);
		annotCount ++ ;
		if(count != null) 
			count[1]++;
		else
			count = new Integer[] {0,1};
		map.put(key, count);
		
		return super.visit(node);
	}

	//2. Enum declaration
	@Override
	public boolean visit(EnumDeclaration node) {
		String key = node.resolveBinding().getQualifiedName();
		Integer[] count = map.get(key);
	
		if(node.resolveBinding().isNested()) {
			nestedCount ++; 
		}
		if(node.resolveBinding().isLocal()) {
			localCount ++; 
		}
		if(node.resolveBinding().isAnonymous()) {
			anonymousCount ++;
		}
		
		if(count != null) 
			count[0]++;
		else
			count = new Integer[] {1,0};
		map.put(key, count);
		return super.visit(node);
	}
	//3-4. Class / Interface declaration
	@Override
	public boolean visit(TypeDeclaration node) {
		String key;
		// COUNTER ADDED -
		// counts for iter3
		if (node.resolveBinding().isNested()) {
			nestedCount ++; 
		}
		
		if (node.resolveBinding().isLocal()) {
			localCount ++; 
			}
		if (node.resolveBinding().isAnonymous()) {
			anonymousCount ++; 
		}
		
		if (!node.resolveBinding().isNested() &
			!node.resolveBinding().isLocal()  &
			!node.resolveBinding().isAnonymous()) {
			otherInterCount ++ ;
		//	System.out.println(node.resolveBinding().getName());
		}
		
		if(node.resolveBinding().getQualifiedName().isEmpty())
			key= node.resolveBinding().getName();
		else
			key = node.resolveBinding().getQualifiedName();
		Integer[] count = map.get(key);
		if(count != null) 
			count[0]++;
		else
			count = new Integer[] {1,0};
		map.put(key, count);
		return super.visit(node);
	}
	//5. Import Declaration
	@Override
	public boolean visit(ImportDeclaration node) {
		String key = node.resolveBinding().getName();
		Integer[] count = map.get(key);
		importDecCount ++ ; 
		if(count != null) 
			count[1]++;
		else
			count = new Integer[] {0,1};
		map.put(key, count);
		return super.visit(node);
	}
	
	public boolean visit(AnonymousClassDeclaration node) {
		if (node.resolveBinding().isAnonymous()) {
			anonymousCount++;
			System.out.println(node.resolveBinding().getQualifiedName());
		}
		
		return super.visit(node);
		
	}


	
}
