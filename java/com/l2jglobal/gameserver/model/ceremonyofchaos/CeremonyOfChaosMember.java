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
package com.l2jglobal.gameserver.model.ceremonyofchaos;

import com.l2jglobal.gameserver.enums.CeremonyOfChaosResult;
import com.l2jglobal.gameserver.model.actor.instance.L2PcInstance;
import com.l2jglobal.gameserver.model.eventengine.AbstractEventMember;

/**
 * @author UnAfraid
 */
public class CeremonyOfChaosMember extends AbstractEventMember<CeremonyOfChaosEvent>
{
	private final int _position;
	private int _lifeTime = 0;
	private CeremonyOfChaosResult _resultType = CeremonyOfChaosResult.LOSE;
	private boolean _isDefeated = false;
	
	public CeremonyOfChaosMember(L2PcInstance player, CeremonyOfChaosEvent event, int position)
	{
		super(player, event);
		_position = position;
	}
	
	public int getPosition()
	{
		return _position;
	}
	
	public void setLifeTime(int time)
	{
		_lifeTime = time;
	}
	
	public int getLifeTime()
	{
		return _lifeTime;
	}
	
	public CeremonyOfChaosResult getResultType()
	{
		return _resultType;
	}
	
	public void setResultType(CeremonyOfChaosResult resultType)
	{
		_resultType = resultType;
	}
	
	public boolean isDefeated()
	{
		return _isDefeated;
	}
	
	public void setDefeated(boolean isDefeated)
	{
		_isDefeated = isDefeated;
	}
}
