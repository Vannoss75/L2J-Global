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
package com.l2jglobal.gameserver.model.events.impl.instance;

import com.l2jglobal.gameserver.model.actor.instance.L2PcInstance;
import com.l2jglobal.gameserver.model.events.EventType;
import com.l2jglobal.gameserver.model.events.impl.IBaseEvent;
import com.l2jglobal.gameserver.model.instancezone.Instance;

/**
 * @author malyelfik
 */
public final class OnInstanceCreated implements IBaseEvent
{
	private final Instance _instance;
	private final L2PcInstance _creator;
	
	public OnInstanceCreated(Instance instance, L2PcInstance creator)
	{
		_instance = instance;
		_creator = creator;
	}
	
	public Instance getInstanceWorld()
	{
		return _instance;
	}
	
	public L2PcInstance getCreator()
	{
		return _creator;
	}
	
	@Override
	public EventType getType()
	{
		return EventType.ON_INSTANCE_CREATED;
	}
}