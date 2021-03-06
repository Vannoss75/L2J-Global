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
package handlers.effecthandlers;

import com.l2jglobal.gameserver.model.StatsSet;
import com.l2jglobal.gameserver.model.effects.AbstractEffect;
import com.l2jglobal.gameserver.model.effects.EffectFlag;
import com.l2jglobal.gameserver.model.skills.BuffInfo;
import com.l2jglobal.gameserver.network.SystemMessageId;
import com.l2jglobal.gameserver.network.serverpackets.ChangeWaitType;
import com.l2jglobal.gameserver.network.serverpackets.Revive;

/**
 * Fake Death effect implementation.
 * @author mkizub
 */
public final class FakeDeath extends AbstractEffect
{
	private final double _power;
	
	public FakeDeath(StatsSet params)
	{
		_power = params.getDouble("power", 0);
		setTicks(params.getInt("ticks"));
	}
	
	@Override
	public long getEffectFlags()
	{
		return EffectFlag.FAKE_DEATH.getMask();
	}
	
	@Override
	public boolean onActionTime(BuffInfo info)
	{
		if (info.getEffected().isDead())
		{
			return false;
		}
		
		final double manaDam = _power * getTicksMultiplier();
		if (manaDam > info.getEffected().getCurrentMp())
		{
			if (info.getSkill().isToggle())
			{
				info.getEffected().sendPacket(SystemMessageId.YOUR_SKILL_WAS_DEACTIVATED_DUE_TO_LACK_OF_MP);
				return false;
			}
		}
		
		info.getEffected().reduceCurrentMp(manaDam);
		
		return info.getSkill().isToggle();
	}
	
	@Override
	public void onExit(BuffInfo info)
	{
		if (info.getEffected().isPlayer())
		{
			info.getEffected().getActingPlayer().setRecentFakeDeath(true);
		}
		
		info.getEffected().broadcastPacket(new ChangeWaitType(info.getEffected(), ChangeWaitType.WT_STOP_FAKEDEATH));
		info.getEffected().broadcastPacket(new Revive(info.getEffected()));
	}
	
	@Override
	public void onStart(BuffInfo info)
	{
		info.getEffected().startFakeDeath();
	}
}
