package semantic_analysis;

/*
 * Trygve IDE
 *   Copyright �2015 James O. Coplien
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 *  For further information about the trygve project, please contact
 *  Jim Coplien at jcoplien@gmail.com
 * 
 */

import mylibrary.SimpleList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import declarations.ActualOrFormalParameterList;
import declarations.Declaration;
import declarations.Declaration.ObjectDeclaration;
import declarations.Declaration.MethodDeclaration;
import declarations.Declaration.ContextDeclaration;
import declarations.Declaration.ClassDeclaration;
import declarations.Declaration.RoleDeclaration;
import declarations.Declaration.StagePropDeclaration;
import declarations.FormalParameterList;
import declarations.Type.BuiltInType;
import declarations.AccessQualifier;
import declarations.Type;
import declarations.Type.ClassType;
import declarations.Type.ContextType;
import declarations.TypeDeclaration;
import error.ErrorLogger;
import error.ErrorLogger.ErrorType;
import add_ons.SystemClass;

public class StaticScope {
	private static StaticScope globalScope_ = new StaticScope(null);
	public static void resetGlobalScope() {
		globalScope_ = new StaticScope(null);
		StaticScope.reinitializeBuiltIns();
		
		// Meta-stuff for boundary conditions
		final Type t = new ClassType(" Class", globalScope_, null);
		final ObjectDeclaration object = new ObjectDeclaration(" Object", t, 0);
		globalScope_.setDeclaration(object);
	}

	private static void reinitializeBuiltIns() {
		if (null == globalScope_.lookupTypeDeclaration("int")) {
			final Type intType = reinitializeInt("int");
			
			reinitializeInt("Integer");
			
			reinitializeDouble();
		
			reinitializeString(intType);
		
			final Type voidType = new BuiltInType("void");
			globalScope_.declareType(voidType);
			
			final Type booleanType = new BuiltInType("boolean");
			globalScope_.declareType(booleanType);
			
			final Type objectType = new BuiltInType("Object");
			globalScope_.declareType(objectType);
			final StaticScope objectsScope = new StaticScope(globalScope_);
			// Should really take itself as a base class....
			final ClassDeclaration objectClass = new ClassDeclaration("Object", objectsScope, null, 157239);
			globalScope_.declareClass(objectClass);
			
			final Type classType = new BuiltInType("Class");
			globalScope_.declareType(classType);
			
			final Type nullType = new BuiltInType("Null");
			globalScope_.declareType(nullType);
			
			SystemClass.setup();
		}
	}
	
	private static Type reinitializeInt(String typeName) {
		final AccessQualifier Public = AccessQualifier.PublicAccess;
		final Type intType = new BuiltInType(typeName);
		final ObjectDeclaration formalParameter = new ObjectDeclaration("rhs", intType, 0);
		final ObjectDeclaration self = new ObjectDeclaration("t$his", intType, 0);
		final FormalParameterList formals = new FormalParameterList();
		formals.addFormalParameter(formalParameter);
		formals.addFormalParameter(self);
		MethodDeclaration methodDecl = new MethodDeclaration("+", intType.enclosedScope(), intType, Public, 0);
		methodDecl.addParameterList(formals);
		intType.enclosedScope().declareMethod(methodDecl);
		methodDecl = new MethodDeclaration("-", intType.enclosedScope(), intType, Public, 0);
		methodDecl.addParameterList(formals);
		intType.enclosedScope().declareMethod(methodDecl);
		methodDecl = new MethodDeclaration("*", intType.enclosedScope(), intType, Public, 0);
		methodDecl.addParameterList(formals);
		intType.enclosedScope().declareMethod(methodDecl);
		methodDecl = new MethodDeclaration("/", intType.enclosedScope(), intType, Public, 0);
		methodDecl.addParameterList(formals);
		intType.enclosedScope().declareMethod(methodDecl);
		methodDecl = new MethodDeclaration("%", intType.enclosedScope(), intType, Public, 0);
		methodDecl.addParameterList(formals);
		intType.enclosedScope().declareMethod(methodDecl);
		globalScope_.declareType(intType);
		return intType;
	}
	
	private static void reinitializeDouble() {
		final Type doubleType = new BuiltInType("double");
		final AccessQualifier Public = AccessQualifier.PublicAccess;
		final ObjectDeclaration formalParameter = new ObjectDeclaration("rhs", doubleType, 0);
		final ObjectDeclaration self = new ObjectDeclaration("t$his", doubleType, 0);
		final FormalParameterList formals = new FormalParameterList();
		formals.addFormalParameter(formalParameter);
		formals.addFormalParameter(self);
		MethodDeclaration methodDecl = new MethodDeclaration("+", doubleType.enclosedScope(), doubleType, Public, 0);
		methodDecl.addParameterList(formals);
		doubleType.enclosedScope().declareMethod(methodDecl);
		methodDecl = new MethodDeclaration("-", doubleType.enclosedScope(), doubleType, Public, 0);
		methodDecl.addParameterList(formals);
		doubleType.enclosedScope().declareMethod(methodDecl);
		methodDecl = new MethodDeclaration("*", doubleType.enclosedScope(), doubleType, Public, 0);
		methodDecl.addParameterList(formals);
		doubleType.enclosedScope().declareMethod(methodDecl);
		methodDecl = new MethodDeclaration("/", doubleType.enclosedScope(), doubleType, Public, 0);
		methodDecl.addParameterList(formals);
		doubleType.enclosedScope().declareMethod(methodDecl);
		globalScope_.declareType(doubleType);
	}
	
	private static void reinitializeString(Type intType) {
		final Type stringType = new BuiltInType("String");
		final AccessQualifier Public = AccessQualifier.PublicAccess;
		final ObjectDeclaration formalParameter = new ObjectDeclaration("rhs", stringType, 0);
		final ObjectDeclaration self = new ObjectDeclaration("t$his", stringType, 0);
		FormalParameterList formals = new FormalParameterList();
		formals.addFormalParameter(formalParameter);
		formals.addFormalParameter(self);
		MethodDeclaration methodDecl = new MethodDeclaration("+", stringType.enclosedScope(), stringType, Public, 0);
		methodDecl.addParameterList(formals);
		stringType.enclosedScope().declareMethod(methodDecl);
		methodDecl = new MethodDeclaration("length", stringType.enclosedScope(), intType, Public, 0);
		methodDecl.signature().setHasConstModifier(true);
		formals = new FormalParameterList();
		methodDecl.addParameterList(formals);
		stringType.enclosedScope().declareMethod(methodDecl);
		globalScope_.declareType(stringType);
	}
	
	public static StaticScope globalScope() { return globalScope_; }
	
	public StaticScope(StaticScope parentScope) {
		parentScope_ = parentScope;
		subScopes_ = new SimpleList();
		if (parentScope_ != null) {
			parentScope_.addSubScope(this);
		}
		objectDeclarationDictionary_ = new HashMap<String, ObjectDeclaration>();
		staticObjectDeclarationDictionary_ = new HashMap<String, ObjectDeclaration>();
		typeDeclarationDictionary_ = new HashMap<String, Type>();
		methodDeclarationDictionary_ = new HashMap<String, ArrayList<MethodDeclaration>>();
		contextDeclarationDictionary_ = new HashMap<String, ContextDeclaration>();
		roleDeclarationDictionary_ = new HashMap<String, RoleDeclaration>();
		classDeclarationDictionary_ = new HashMap<String, ClassDeclaration>();
		hasDeclarationsThatAreLostBetweenPasses_ = false;
	}
	
	public StaticScope(StaticScope parentScope, boolean losesMemory) {
		this(parentScope);
		hasDeclarationsThatAreLostBetweenPasses_ = losesMemory;
	}
	
	public void declare(Declaration decl) {
		assert false;
	}
	
	public boolean hasDeclarationsThatAreLostBetweenPasses() {
		return hasDeclarationsThatAreLostBetweenPasses_;
	}
	
	public String name() {
		String retval = null;
		if (associatedDeclaration_ != null) {
			retval = associatedDeclaration_.name();
		} else {
			retval = "*unknown*";
		}
		assert retval != null;
		return retval;
	}
	
	public void declareContext(ContextDeclaration decl) {
		final String contextName = decl.name();
		if (contextDeclarationDictionary_.containsKey(contextName)) {
			ErrorLogger.error(ErrorType.Fatal, "Multiple definitions of context ", contextName, " in ", name());
		} else {
			contextDeclarationDictionary_.put(contextName, decl);
			if (null != parentScope_) parentScope_.checkMegaTypeShadowing(decl);
		}
	}
	
	private void checkMegaTypeShadowing(TypeDeclaration decl) {
		final StaticScope parent = this.parentScope();
		if (null != parent) {
			final String name = decl.name();
			TypeDeclaration collidingDeclaration = null;
			if (name.equals(decl.name())) {
				boolean collision = false;
				if (contextDeclarationDictionary_.containsKey(name)) {
					collidingDeclaration = contextDeclarationDictionary_.get(name);
					collision = true;
				} else if (classDeclarationDictionary_.containsKey(name)) {
					collidingDeclaration = classDeclarationDictionary_.get(name);
					collision = true;
				} else if (roleDeclarationDictionary_.containsKey(name)) {
					collidingDeclaration = roleDeclarationDictionary_.get(name);
					collision = true;
				}
				if (collision) {
					ErrorLogger.error(ErrorType.Warning, decl.lineNumber(), "Declaration hides ", name, " declaration at line ",
							Integer.toString(collidingDeclaration.lineNumber()));
				}
			}
			parent.checkMegaTypeShadowing(decl);
		}
	}
	
	public ContextDeclaration lookupContextDeclarationRecursive(String contextName)
	{
		ContextDeclaration retval = this.lookupContextDeclaration(contextName);
		if (null == retval) {
			if (null != parentScope_) {
				retval = parentScope_.lookupContextDeclarationRecursive(contextName);
			}
		}
		return retval;
	}
	public ContextDeclaration lookupContextDeclaration(String contextName) {
		ContextDeclaration retval = null;
		if (contextDeclarationDictionary_.containsKey(contextName)) {
			retval = contextDeclarationDictionary_.get(contextName);
		}
		return retval;
	}
	
	public void declareRole(RoleDeclaration decl) {
		final String roleName = decl.name();
		if (roleDeclarationDictionary_.containsKey(roleName)) {
			ErrorLogger.error(ErrorType.Fatal, "Multiple definitions of role ", roleName, " in ", name());
		} else {
			roleDeclarationDictionary_.put(roleName, decl);
		}
		if (null != parentScope_) parentScope_.checkMegaTypeShadowing(decl);
	}
	
	public RoleDeclaration lookupRoleDeclarationRecursive(String roleName) {
		RoleDeclaration retval = this.lookupRoleDeclaration(roleName);
		if (null == retval) {
			// Stop searching at Context boundary. If there are nested
			// Contexts we don't want to go wandering into THAT Context
			// and pick up a role. Also stop at Class boundaries � it
			// doesn't make sense to refer to a role inside of a class
			final Declaration associatedDeclaration = this.associatedDeclaration();
			final Type myType = null != associatedDeclaration? associatedDeclaration.type(): null;
			if (myType instanceof ClassType || myType instanceof ContextType) {
				retval = null;	// redundant, but clear
			} else if (null != parentScope_) {
				retval = parentScope_.lookupRoleDeclarationRecursive(roleName);
			}
		}
		return retval;
	}
	public RoleDeclaration lookupRoleDeclaration(String roleName) {
		RoleDeclaration retval = null;
		if (roleDeclarationDictionary_.containsKey(roleName)) {
			retval = roleDeclarationDictionary_.get(roleName);
		}
		return retval;
	}
	
	public void declareClass(ClassDeclaration decl) {
		final String className = decl.name();
		if (classDeclarationDictionary_.containsKey(className))
		{
			ErrorLogger.error(ErrorType.Fatal, "Multiple definitions of class ", className, " in ", name());
		} else {
			classDeclarationDictionary_.put(className, decl);
		}
		if (null != parentScope_) parentScope_.checkMegaTypeShadowing(decl);
	}
	
	public ClassDeclaration lookupClassDeclarationRecursive(String className) {
		ClassDeclaration retval = this.lookupClassDeclaration(className);
		if (null == retval) {
			if (null != parentScope_) {
				retval = parentScope_.lookupClassDeclarationRecursive(className);
			}
		}
		return retval;
	}
	public ClassDeclaration lookupClassDeclaration(String className) {
		ClassDeclaration retval = null;
		if (classDeclarationDictionary_.containsKey(className)) {
			retval = classDeclarationDictionary_.get(className);
		}
		return retval;
	}
	
	public void declareMethod(MethodDeclaration decl) {
		final String methodName = decl.name();

		if (methodDeclarationDictionary_.containsKey(methodName)) {
			final ArrayList<MethodDeclaration> oldEntry = methodDeclarationDictionary_.get(methodName);
			for (MethodDeclaration aDecl : oldEntry) {
				final FormalParameterList loggedSignature = aDecl.formalParameterList();
				if (null == loggedSignature && null == decl.formalParameterList()) {
					ErrorLogger.error(ErrorType.Fatal, "Multiple definitions of method ", methodName, " in ", name());
					break;
				} else if (loggedSignature.alignsWith(decl.formalParameterList())) {
					ErrorLogger.error(ErrorType.Fatal, "Multiple definitions of method ", methodName, " in ", name());
					break;
				}
			}
			oldEntry.add(decl);
		} else {
			final ArrayList<MethodDeclaration> newEntry = new ArrayList<MethodDeclaration>();
			newEntry.add(decl);
			methodDeclarationDictionary_.put(methodName, newEntry);
		}
		if (null != parentScope_) parentScope_.checkMethodShadowing(decl);
	}
	
	private void checkMethodShadowing(MethodDeclaration decl) {
		final StaticScope parent = this.parentScope();
		if (null != parent) {
			final String name = decl.name();
			MethodDeclaration collidingDeclaration = null;
			if (name.equals(decl.name())) {
				boolean collision = false;
				if (methodDeclarationDictionary_.containsKey(name)) {
					final ArrayList<MethodDeclaration> oldEntry = methodDeclarationDictionary_.get(name);
					for (MethodDeclaration aDecl : oldEntry) {
						final FormalParameterList loggedSignature = aDecl.formalParameterList();
						if (null == loggedSignature && null == decl.formalParameterList()) {
							collidingDeclaration = aDecl;
							collision = true;
							break;
						} else if (loggedSignature.alignsWith(decl.formalParameterList())) {
							collidingDeclaration = aDecl;
							collision = true;
							break;
						}
					}
				}
				if (collision) {
					ErrorLogger.error(ErrorType.Fatal, decl.lineNumber(), "Method ", name, " hides method of same name at line ",
							Integer.toString(collidingDeclaration.lineNumber()));
				}
			}
			parent.checkMethodShadowing(decl);
		}
	}
	
	public void declareType(Type typeDecl) {
		final String typeName = typeDecl.name();
		if (typeDeclarationDictionary_.containsKey(typeName)) {
			ErrorLogger.error(ErrorType.Fatal, "Multiple definitions of type ", typeName, " in ", name());
		} else {
			if (this.lookupTypeDeclarationRecursive(typeName) != null) {
				ErrorLogger.error(ErrorType.Fatal, "Type declaration of ", typeName, " might hide declaration in enclosing scope", "");
			} else {
				typeDeclarationDictionary_.put(typeName, typeDecl);
			}
		}
	}
	
	public Type lookupTypeDeclarationRecursive(String typeName) {
		Type retval = this.lookupTypeDeclaration(typeName);
		if (null == retval) {
			if (null != parentScope_) {
				retval = parentScope_.lookupTypeDeclarationRecursive(typeName);
			}
		}
		return retval;
	}
	public Type lookupTypeDeclaration(String simpleTypeName) {
		Type retval = null;
		if (typeDeclarationDictionary_.containsKey(simpleTypeName)) {
			retval = typeDeclarationDictionary_.get(simpleTypeName);
		}
		return retval;
	}
	
	public void declareObject(ObjectDeclaration decl) {
		final String objectName = decl.name();
		if (objectDeclarationDictionary_.containsKey(objectName)) {
			ErrorLogger.error(ErrorType.Fatal, "Multiple definitions of object ", objectName, " in ", name());
		} else {
			reDeclareObject(decl);
		}
	}
	
	public void reDeclareObject(ObjectDeclaration decl) {
		final String objectName = decl.name();
		objectDeclarationDictionary_.put(objectName, decl);
		decl.setEnclosingScope(this);
		if (null != parentScope_) parentScope_.checkObjectDeclarationShadowing(decl);
	}
	
	public void declareStaticObject(ObjectDeclaration decl) {
		final String objectName = decl.name();
		if (objectDeclarationDictionary_.containsKey(objectName)) {
			ErrorLogger.error(ErrorType.Fatal, "Multiple definitions of object ", objectName, " in ", name());
		} else if (staticObjectDeclarationDictionary_.containsKey(objectName)) {
			ErrorLogger.error(ErrorType.Fatal, "Multiple definitions of object: static ", objectName, " in ", name());
		} else {
			// Make sure that this is a class scope
			final TypeDeclaration associatedDeclaration = (TypeDeclaration)this.associatedDeclaration();
			assert null != associatedDeclaration;
			
			if (!(associatedDeclaration instanceof TypeDeclaration)) {
				ErrorLogger.error(ErrorType.Fatal, 0, "Static member ", objectName, " in ", name(),
						" may be declared only in a class, Context or Role", "");
			} else {
				associatedDeclaration.declareStaticObject(decl);
				staticObjectDeclarationDictionary_.put(objectName, decl);
			}
		}
	}
	
	public Map<String, ObjectDeclaration> staticObjectDeclarations() {
		return staticObjectDeclarationDictionary_;
	}
	
	private void checkObjectDeclarationShadowing(ObjectDeclaration decl) {
		final StaticScope parent = this.parentScope();
		if (null != parent) {
			final String name = decl.name();
			ObjectDeclaration collidingDeclaration = null;
			if (name.equals(decl.name())) {
				boolean collision = false;
				if (objectDeclarationDictionary_.containsKey(name)) {
					collidingDeclaration = objectDeclarationDictionary_.get(name);
					collision = true;
				}
				if (collision) {
					ErrorLogger.error(ErrorType.Fatal, decl.lineNumber(), "Declaration of ", name, " may hide declaration at line ",
							Integer.toString(collidingDeclaration.lineNumber()));
				}
			}
			parent.checkObjectDeclarationShadowing(decl);
		}
	}

	
	public ObjectDeclaration lookupObjectDeclarationRecursive(String simpleIDName) {
		ObjectDeclaration retval = this.lookupObjectDeclaration(simpleIDName);
		if (null == retval) {
			// Stop searching at Class or Context boundary. If there are nested
			// Classes or Contexts we don't want to go wandering into THAT scope
			// and pick up a declaration.
			final Declaration associatedDeclaration = this.associatedDeclaration();
			final Type myType = null != associatedDeclaration? associatedDeclaration.type(): null;
			if (myType instanceof ClassType || myType instanceof ContextType) {
				retval = null;	// redundant, but clear
			} else if (null != parentScope_) {
				retval = parentScope_.lookupObjectDeclarationRecursive(simpleIDName);
			}
		}
		return retval;
	}
	public ObjectDeclaration lookupObjectDeclarationRecursiveWithinMethod(String simpleIDName) {
		ObjectDeclaration retval = this.lookupObjectDeclaration(simpleIDName);
		if (null == retval) {
			// Like lookupObjectDeclarationRecursive, but it will also stop if
			// it encounters a method boundary. It searches for variables only
			// that are on the activation record (in any level of scope) within
			// the current method.
			final Declaration associatedDeclaration = this.associatedDeclaration();
			final Type myType = null != associatedDeclaration? associatedDeclaration.type(): null;
			if (myType instanceof ClassType || myType instanceof ContextType) {
				retval = null;	// redundant, but clear
			} else if (associatedDeclaration instanceof MethodDeclaration) {
				retval = null;	// redundant, but clear
			} else if (null != parentScope_) {
				retval = parentScope_.lookupObjectDeclarationRecursive(simpleIDName);
			}
		}
		return retval;
	}
	public ObjectDeclaration lookupObjectDeclaration(String simpleIDName) {
		ObjectDeclaration retval = null;
		if (objectDeclarationDictionary_.containsKey(simpleIDName)) {
			retval = objectDeclarationDictionary_.get(simpleIDName);
		} else if (staticObjectDeclarationDictionary_.containsKey(simpleIDName)) {
			final TypeDeclaration associatedDeclaration = (TypeDeclaration)this.associatedDeclaration();
			retval = associatedDeclaration.lookupStaticObjectDeclaration(simpleIDName);
		}
		return retval;
	}
	
	public MethodDeclaration lookupMethodDeclarationRecursive(String methodSelector,
			ActualOrFormalParameterList parameterList,
			boolean ignoreSignature) {
		MethodDeclaration retval = this.lookupMethodDeclaration(methodSelector, parameterList, ignoreSignature);
		if (null == retval) {
			// If this is a class, see if the base class has it
			if (associatedDeclaration_ instanceof ClassDeclaration) {
				final ClassDeclaration thisClass = (ClassDeclaration)associatedDeclaration_;
				final ClassDeclaration baseClass = thisClass.baseClass();
				if (null != baseClass) {
					final StaticScope baseClassScope = baseClass.enclosedScope();
					retval = baseClassScope.lookupMethodDeclarationRecursive(methodSelector, parameterList, ignoreSignature);
				}
			}
		}
		if (null == retval) {
			if (null != parentScope_) {
				retval = parentScope_.lookupMethodDeclarationRecursive(methodSelector, parameterList, ignoreSignature);
			}
		}
		return retval;
	}
	public MethodDeclaration lookupMethodDeclaration(String methodSelector, ActualOrFormalParameterList parameterList,
			boolean ignoreSignature) {
		MethodDeclaration retval = null;
		if (methodDeclarationDictionary_.containsKey(methodSelector)) {
			final ArrayList<MethodDeclaration> oldEntry = methodDeclarationDictionary_.get(methodSelector);
			for (MethodDeclaration aDecl : oldEntry) {
				final FormalParameterList loggedSignature = aDecl.formalParameterList();
				if (ignoreSignature) {
					retval = aDecl; break;
				} else if (null == loggedSignature && null == parameterList) {
					retval = aDecl; break;
				} else if (null != loggedSignature && loggedSignature.alignsWith(parameterList)) {
					retval = aDecl; break;
				}
			}
		}
		return retval;
	}
	public MethodDeclaration lookupMethodDeclarationIgnoringParameter(String methodSelector, ActualOrFormalParameterList parameterList,
			String paramToIgnore) {
		MethodDeclaration retval = null;
		if (methodDeclarationDictionary_.containsKey(methodSelector)) {
			final ArrayList<MethodDeclaration> oldEntry = methodDeclarationDictionary_.get(methodSelector);
			for (MethodDeclaration aDecl : oldEntry) {
				final FormalParameterList loggedSignature = aDecl.formalParameterList();
				if (null == loggedSignature && null == parameterList) {
					retval = aDecl; break;
				} else if (null != loggedSignature && FormalParameterList.alignsWithParameterListIgnoringParam(loggedSignature, parameterList, paramToIgnore)) {
					retval = aDecl; break;
				}
			}
		}
		return retval;
	}
	
	
	public MethodDeclaration lookupMethodDeclarationRecursiveWithLineNumber(String methodSelector, int lineNumber) {
		MethodDeclaration retval = this.lookupMethodDeclarationWithLineNumber(methodSelector, lineNumber);
		if (null == retval) {
			// If this is a class, see if the base class has it
			if (associatedDeclaration_ instanceof ClassDeclaration) {
				final ClassDeclaration thisClass = (ClassDeclaration)associatedDeclaration_;
				final ClassDeclaration baseClass = thisClass.baseClass();
				final StaticScope baseClassScope = baseClass.enclosedScope();
				retval = baseClassScope.lookupMethodDeclarationRecursiveWithLineNumber(methodSelector, lineNumber);
			}
		}
		if (null == retval) {
			if (null != parentScope_) {
				retval = parentScope_.lookupMethodDeclarationRecursiveWithLineNumber(methodSelector, lineNumber);
			}
		}
		return retval;
	}
	public MethodDeclaration lookupMethodDeclarationWithLineNumber(String methodSelector, int lineNumber) {
		MethodDeclaration retval = null;
		if (methodDeclarationDictionary_.containsKey(methodSelector)) {
			final ArrayList<MethodDeclaration> oldEntry = methodDeclarationDictionary_.get(methodSelector);
			for (MethodDeclaration aDecl : oldEntry) {
				if (aDecl.lineNumber() == lineNumber) {
					retval = aDecl;
					break;
				}
			}
		}
		return retval;
	}
	
	// Scope management
	public void setDeclaration(Declaration associatedDeclaration) {
		associatedDeclaration_ = associatedDeclaration;
	}
	public Declaration associatedDeclaration() {
		return associatedDeclaration_;
	}
	public void addSubScope(StaticScope child) {
		subScopes_.add(child);
	}
	public StaticScope parentScope() {
		return parentScope_;
	}
	
	public List<ObjectDeclaration> objectDeclarations() {
		List<ObjectDeclaration> retval = new ArrayList<ObjectDeclaration>();
		for (Map.Entry<String, ObjectDeclaration> objectDecl : objectDeclarationDictionary_.entrySet()) {
			retval.add(objectDecl.getValue());
		}
		return retval;
	}
	public List<ClassDeclaration> classDeclarations() {
		List<ClassDeclaration> retval = new ArrayList<ClassDeclaration>();
		for (Map.Entry<String, ClassDeclaration> classDecl : classDeclarationDictionary_.entrySet()) {
			retval.add(classDecl.getValue());
		}
		return retval;
	}
	public List<ContextDeclaration> contextDeclarations() {
		List<ContextDeclaration> retval = new ArrayList<ContextDeclaration>();
		for (Map.Entry<String, ContextDeclaration> contextDecl : contextDeclarationDictionary_.entrySet()) {
			retval.add(contextDecl.getValue());
		}
		return retval;
	}
	public List<MethodDeclaration> methodDeclarations() {
		List<MethodDeclaration> retval = new ArrayList<MethodDeclaration>();
		for (Map.Entry<String, ArrayList<MethodDeclaration>> iter : methodDeclarationDictionary_.entrySet()) {
			for (MethodDeclaration mDecl : iter.getValue()) {
				retval.add(mDecl);
			}
		}
		return retval;
	}
	public List<StagePropDeclaration> stagePropDeclarations() {
		List<StagePropDeclaration> retval = new ArrayList<StagePropDeclaration>();
		for (Map.Entry<String, RoleDeclaration> stagePropDecl : roleDeclarationDictionary_.entrySet()) {
			final Declaration d = stagePropDecl.getValue();
			if (d instanceof StagePropDeclaration) {
				retval.add((StagePropDeclaration)d);
			}
		}
		return retval;
	}
	public List<RoleDeclaration> roleDeclarations() {
		List<RoleDeclaration> retval = new ArrayList<RoleDeclaration>();
		for (Map.Entry<String, RoleDeclaration> roleDecl : roleDeclarationDictionary_.entrySet()) {
			retval.add(roleDecl.getValue());
		}
		return retval;
	}
	public String pathName() {
		return associatedDeclaration_.type().pathName();
	}
	
	public static class StaticRoleScope extends StaticScope {
		public StaticRoleScope(StaticScope parentScope) {
			super(parentScope);
			requiredMethodDeclarationDictionary_ = new HashMap<String,ArrayList<MethodDeclaration>>();
		}
		public void declareMethod(MethodDeclaration decl) {
			boolean dup = false;
			final String methodName = decl.name();
		
			if (requiredMethodDeclarationDictionary_.containsKey(methodName)) {
				final ArrayList<MethodDeclaration> oldEntry = requiredMethodDeclarationDictionary_.get(methodName);
				for (MethodDeclaration aDecl : oldEntry) {
					final FormalParameterList loggedSignature = aDecl.formalParameterList();
					if (null == loggedSignature && null == decl.formalParameterList()) {
						dup = true;
						break;
					} else if (loggedSignature.alignsWith(decl.formalParameterList())) {
						dup = true;
						break;
					}
				}
			}
			
			if (dup) {
				ErrorLogger.error(ErrorType.Fatal, decl.lineNumber(), "Declaration of `", methodName, "� in ",
					name(), " would create multiple methods of the same name in the same object.", "");
			} else {
				super.declareMethod(decl);
			}
		}
		public void declareRequiredMethod(MethodDeclaration decl) {
			final String methodName = decl.name();
			
			final MethodDeclaration lookupExistingEntry = this.lookupMethodDeclaration(methodName,
					decl.formalParameterList(), true);
			if (null != lookupExistingEntry) {
				ErrorLogger.error(ErrorType.Fatal, decl.lineNumber(), "Declaration of `", methodName, "� in ",
						name(), " would create multiple methods of the same name in the same object.", "");
			} else if (requiredMethodDeclarationDictionary_.containsKey(methodName)) {
				final ArrayList<MethodDeclaration> oldEntry = requiredMethodDeclarationDictionary_.get(methodName);
				for (MethodDeclaration aDecl : oldEntry) {
					final FormalParameterList loggedSignature = aDecl.formalParameterList();
					if (null == loggedSignature && null == decl.formalParameterList()) {
						ErrorLogger.error(ErrorType.Fatal, "Multiple declarations of `required� method ", methodName, " in ", name());
						break;
					} else if (loggedSignature.alignsWith(decl.formalParameterList())) {
						ErrorLogger.error(ErrorType.Fatal, "Multiple declarations of `required� method ", methodName, " in ", name());
						break;
					}
				}
				oldEntry.add(decl);
			} else {
				final ArrayList<MethodDeclaration> newEntry = new ArrayList<MethodDeclaration>();
				newEntry.add(decl);
				requiredMethodDeclarationDictionary_.put(methodName, newEntry);
			}
			if (null != parentScope()) parentScope().checkMethodShadowing(decl);
		}
		public List<MethodDeclaration> methodDeclarations() {
			List<MethodDeclaration> retval = super.methodDeclarations();

			for (Map.Entry<String, ArrayList<MethodDeclaration>> iter : requiredMethodDeclarationDictionary_.entrySet()) {
				for (MethodDeclaration mDecl : iter.getValue()) {
					retval.add(mDecl);
				}
			}
			return retval;
		}
		public MethodDeclaration lookupMethodDeclaration(String methodSelector, ActualOrFormalParameterList parameterList,
				boolean ignoreSignature) {
			MethodDeclaration retval = super.lookupMethodDeclaration(methodSelector, parameterList,
					 ignoreSignature);
			if (null == retval) {
				if (requiredMethodDeclarationDictionary_.containsKey(methodSelector)) {
					final ArrayList<MethodDeclaration> oldEntry = requiredMethodDeclarationDictionary_.get(methodSelector);
					for (MethodDeclaration aDecl : oldEntry) {
						final FormalParameterList loggedSignature = aDecl.formalParameterList();
						if (ignoreSignature) {
							retval = aDecl; break;
						} else if (null == loggedSignature && null == parameterList) {
							retval = aDecl; break;
						} else if (null != loggedSignature && loggedSignature.alignsWith(parameterList)) {
							retval = aDecl; break;
						}
					}
				}
			}
			return retval;
		}
		public MethodDeclaration lookupMethodDeclarationIgnoringParameter(String methodSelector, ActualOrFormalParameterList parameterList,
				String paramToIgnore) {
			MethodDeclaration retval = super.lookupMethodDeclarationIgnoringParameter(methodSelector, parameterList,
					 paramToIgnore);
			if (null == retval) {
				if (requiredMethodDeclarationDictionary_.containsKey(methodSelector)) {
					final ArrayList<MethodDeclaration> oldEntry = requiredMethodDeclarationDictionary_.get(methodSelector);
					for (MethodDeclaration aDecl : oldEntry) {
						final FormalParameterList loggedSignature = aDecl.formalParameterList();
						if (null == loggedSignature && null == parameterList) {
							retval = aDecl; break;
						} else if (null != loggedSignature && FormalParameterList.alignsWithParameterListIgnoringParam(loggedSignature, parameterList, paramToIgnore)) {
							retval = aDecl; break;
						}
					}
				}
			}
			return retval;
		}
		
		private Map<String,ArrayList<MethodDeclaration>> requiredMethodDeclarationDictionary_;
	}
	
	private final StaticScope parentScope_;
	private SimpleList subScopes_;
	protected Declaration associatedDeclaration_;
	private final Map<String,ObjectDeclaration> objectDeclarationDictionary_;
	private final Map<String,ObjectDeclaration> staticObjectDeclarationDictionary_;
	private final Map<String,Type> typeDeclarationDictionary_;
	private final Map<String,ArrayList<MethodDeclaration>> methodDeclarationDictionary_;
	private final Map<String,ContextDeclaration> contextDeclarationDictionary_;
	private final Map<String,ClassDeclaration> classDeclarationDictionary_;
	private Map<String,RoleDeclaration> roleDeclarationDictionary_;
	private boolean hasDeclarationsThatAreLostBetweenPasses_;
}
