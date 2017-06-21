/*
 * This file is part of the L2J Global project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jglobal.gameserver.model.skills;

/**
 * @author Nik
 */
public enum SkillCastingType
{
	SIMULTANEOUS(-1),
	NORMAL(0),
	NORMAL_SECOND(1),
	BLUE(2),
	GREEN(3),
	RED(4);
	
	private final int _clientBarId;
	
	SkillCastingType(int clientBarId)
	{
		_clientBarId = clientBarId;
	}
	
	public int getClientBarId()
	{
		return _clientBarId;
	}
}
