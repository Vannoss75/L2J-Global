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

import com.l2jglobal.gameserver.instancemanager.PunishmentManager;
import com.l2jglobal.gameserver.model.StatsSet;
import com.l2jglobal.gameserver.model.actor.L2Character;
import com.l2jglobal.gameserver.model.effects.AbstractEffect;
import com.l2jglobal.gameserver.model.effects.EffectFlag;
import com.l2jglobal.gameserver.model.punishment.PunishmentAffect;
import com.l2jglobal.gameserver.model.punishment.PunishmentTask;
import com.l2jglobal.gameserver.model.punishment.PunishmentType;
import com.l2jglobal.gameserver.model.skills.BuffInfo;
import com.l2jglobal.gameserver.model.skills.Skill;

/**
 * Block Chat effect implementation.
 * @author BiggBoss
 */
public final class BlockChat extends AbstractEffect
{
	public BlockChat(StatsSet params)
	{
	}
	
	@Override
	public boolean canStart(BuffInfo info)
	{
		return (info.getEffected() != null) && info.getEffected().isPlayer();
	}
	
	@Override
	public long getEffectFlags()
	{
		return EffectFlag.CHAT_BLOCK.getMask();
	}
	
	@Override
	public void onStart(L2Character effector, L2Character effected, Skill skill)
	{
		PunishmentManager.getInstance().startPunishment(new PunishmentTask(0, effected.getObjectId(), PunishmentAffect.CHARACTER, PunishmentType.CHAT_BAN, 0, "Chat banned bot report", "system", true));
	}
	
	@Override
	public void onExit(BuffInfo info)
	{
		PunishmentManager.getInstance().stopPunishment(info.getEffected().getObjectId(), PunishmentAffect.CHARACTER, PunishmentType.CHAT_BAN);
	}
}
