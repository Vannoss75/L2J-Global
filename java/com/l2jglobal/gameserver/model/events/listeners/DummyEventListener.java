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
package com.l2jglobal.gameserver.model.events.listeners;

import com.l2jglobal.gameserver.model.events.EventType;
import com.l2jglobal.gameserver.model.events.ListenersContainer;
import com.l2jglobal.gameserver.model.events.impl.IBaseEvent;
import com.l2jglobal.gameserver.model.events.returns.AbstractEventReturn;

/**
 * Runnable event listener provides callback operation without any parameters and return object.
 * @author UnAfraid
 */
public class DummyEventListener extends AbstractEventListener
{
	public DummyEventListener(ListenersContainer container, EventType type, Object owner)
	{
		super(container, type, owner);
	}
	
	@Override
	public <R extends AbstractEventReturn> R executeEvent(IBaseEvent event, Class<R> returnBackClass)
	{
		return null;
	}
}
