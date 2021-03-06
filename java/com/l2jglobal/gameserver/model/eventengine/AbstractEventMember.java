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
package com.l2jglobal.gameserver.model.eventengine;

import java.util.concurrent.atomic.AtomicInteger;

import com.l2jglobal.gameserver.model.L2World;
import com.l2jglobal.gameserver.model.actor.instance.L2PcInstance;
import com.l2jglobal.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author UnAfraid
 * @param <T>
 */
public abstract class AbstractEventMember<T extends AbstractEvent<?>>
{
	private final int _objectId;
	private final T _event;
	private final AtomicInteger _score = new AtomicInteger();
	
	public AbstractEventMember(L2PcInstance player, T event)
	{
		_objectId = player.getObjectId();
		_event = event;
	}
	
	public final int getObjectId()
	{
		return _objectId;
	}
	
	public L2PcInstance getPlayer()
	{
		return L2World.getInstance().getPlayer(_objectId);
	}
	
	public void sendPacket(IClientOutgoingPacket... packets)
	{
		final L2PcInstance player = getPlayer();
		if (player != null)
		{
			for (IClientOutgoingPacket packet : packets)
			{
				player.sendPacket(packet);
			}
		}
	}
	
	public int getClassId()
	{
		final L2PcInstance player = getPlayer();
		if (player != null)
		{
			return player.getClassId().getId();
		}
		return 0;
	}
	
	public void setScore(int score)
	{
		_score.set(score);
	}
	
	public int getScore()
	{
		return _score.get();
	}
	
	public int incrementScore()
	{
		return _score.incrementAndGet();
	}
	
	public int decrementScore()
	{
		return _score.decrementAndGet();
	}
	
	public int addScore(int score)
	{
		return _score.addAndGet(score);
	}
	
	public final T getEvent()
	{
		return _event;
	}
}
