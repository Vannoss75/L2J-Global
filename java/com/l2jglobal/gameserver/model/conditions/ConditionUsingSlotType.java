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
package com.l2jglobal.gameserver.model.conditions;

import com.l2jglobal.gameserver.model.actor.L2Character;
import com.l2jglobal.gameserver.model.items.L2Item;
import com.l2jglobal.gameserver.model.skills.Skill;

/**
 * @author NosBit
 */
public class ConditionUsingSlotType extends Condition
{
	private final int _mask;
	
	public ConditionUsingSlotType(int mask)
	{
		_mask = mask;
	}
	
	@Override
	public boolean testImpl(L2Character effector, L2Character effected, Skill skill, L2Item item)
	{
		if ((effector == null) || !effector.isPlayer())
		{
			return false;
		}
		
		return (effector.getActiveWeaponItem().getBodyPart() & _mask) != 0;
	}
	
}