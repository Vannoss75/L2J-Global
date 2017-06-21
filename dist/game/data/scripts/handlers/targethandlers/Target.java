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
package handlers.targethandlers;

import com.l2jglobal.gameserver.GeoData;
import com.l2jglobal.gameserver.handler.ITargetTypeHandler;
import com.l2jglobal.gameserver.model.L2Object;
import com.l2jglobal.gameserver.model.actor.L2Character;
import com.l2jglobal.gameserver.model.skills.Skill;
import com.l2jglobal.gameserver.model.skills.targets.TargetType;
import com.l2jglobal.gameserver.network.SystemMessageId;
import com.l2jglobal.gameserver.network.serverpackets.FlyToLocation.FlyType;

/**
 * Any friendly selected target or enemy if force use. Works on dead targets or doors as well.
 * @author Nik
 */
public class Target implements ITargetTypeHandler
{
	@Override
	public Enum<TargetType> getTargetType()
	{
		return TargetType.TARGET;
	}
	
	@Override
	public L2Object getTarget(L2Character activeChar, L2Object selectedTarget, Skill skill, boolean forceUse, boolean dontMove, boolean sendMessage)
	{
		if (selectedTarget == null)
		{
			return null;
		}
		
		if (!selectedTarget.isCharacter())
		{
			if (sendMessage)
			{
				activeChar.sendPacket(SystemMessageId.INVALID_TARGET);
			}
			return null;
		}
		
		final L2Character target = (L2Character) selectedTarget;
		
		// You can always target yourself.
		if (activeChar == target)
		{
			return target;
		}
		
		// Check for cast range if character cannot move. TODO: char will start follow until within castrange, but if his moving is blocked by geodata, this msg will be sent.
		if (dontMove)
		{
			if (activeChar.calculateDistance(target, false, false) > skill.getCastRange())
			{
				if (sendMessage)
				{
					activeChar.sendPacket(SystemMessageId.THE_DISTANCE_IS_TOO_FAR_AND_SO_THE_CASTING_HAS_BEEN_STOPPED);
				}
				
				return null;
			}
		}
		
		if ((skill.getFlyType() == FlyType.CHARGE) && !GeoData.getInstance().canMove(activeChar, target))
		{
			if (sendMessage)
			{
				activeChar.sendPacket(SystemMessageId.THE_TARGET_IS_LOCATED_WHERE_YOU_CANNOT_CHARGE);
			}
			return null;
		}
		
		// Geodata check when character is within range.
		if (!GeoData.getInstance().canSeeTarget(activeChar, target))
		{
			if (sendMessage)
			{
				activeChar.sendPacket(SystemMessageId.CANNOT_SEE_TARGET);
			}
			
			return null;
		}
		return target;
	}
}
