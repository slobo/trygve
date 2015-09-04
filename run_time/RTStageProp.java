package run_time;

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

import java.util.HashMap;
import java.util.Map;

import declarations.Declaration.StagePropDeclaration;
import declarations.Type;

public class RTStageProp extends RTClassAndContextCommon implements RTType  {
	public RTStageProp(StagePropDeclaration associatedDeclaration) {
		super(associatedDeclaration);
		assert associatedDeclaration instanceof StagePropDeclaration;
		associatedDeclaration_ = associatedDeclaration;
		stringToContextDeclMap_ = new HashMap<String, RTContext>();
	}
	public StagePropDeclaration associatedDeclaration() {
		return associatedDeclaration_;
	}
	@Override public void addClass(String typeName, RTClass classDecl) {
		assert false;
	}
	@Override public void addContext(String typeName, RTContext contextDecl) {
		stringToContextDeclMap_.put(typeName, contextDecl);
	}
	public void addObject(String objectName, Type objectType) {
		assert false;
	}
	@Override public void addStageProp(String stagePropName, RTStageProp stagePropType) {
		assert false;
	}
	@Override public void addRole(String roleName, RTRole roleType) {
		assert false;
	}
	@Override public RTType typeNamed(String name) {
		assert false;
		return null;
	}
	@Override public void setObject(String unused1, RTObject unused2) {
		assert false;
	}
	@Override public RTObject getObject(String objectName) {
		assert false;
		// Doesn't seem right to get an instance from a stageprop.
		return null;
	}
	@Override public void addObjectDeclaration(String unused1, RTType unused2) {
		assert false;
	}
	@Override public Map<String, RTType> objectDeclarations() {
		assert false;
		return null;
	}
	@Override public Map<String, RTRole> nameToRoleDeclMap() {
		return new HashMap<String, RTRole>();
	}
	
	private Map<String, RTContext> stringToContextDeclMap_;
	private StagePropDeclaration associatedDeclaration_;
}
