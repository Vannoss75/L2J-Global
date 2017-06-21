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

/**
 * Block escape effect implementation
 * @author UnAfraid
 */
public class BlockEscape extends AbstractEffect
{
	public BlockEscape(StatsSet params)
	{
	}
	
	@Override
	public long getEffectFlags()
	{
		return EffectFlag.CANNOT_ESCAPE.getMask();
	}
}
