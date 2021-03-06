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
package com.l2jglobal.gameserver.model.events.timers;

import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.l2jglobal.gameserver.ThreadPoolManager;
import com.l2jglobal.gameserver.instancemanager.TimersManager;
import com.l2jglobal.gameserver.model.StatsSet;
import com.l2jglobal.gameserver.model.actor.L2Npc;
import com.l2jglobal.gameserver.model.actor.instance.L2PcInstance;
import com.l2jglobal.gameserver.model.events.TimerExecutor;

/**
 * @author UnAfraid
 * @param <T>
 */
public class TimerHolder<T> implements Runnable
{
	private final T _event;
	private final StatsSet _params;
	private final long _time;
	private final L2Npc _npc;
	private final L2PcInstance _player;
	private final boolean _isRepeating;
	private final IEventTimerEvent<T> _eventScript;
	private final IEventTimerCancel<T> _cancelScript;
	private final TimerExecutor<T> _postExecutor;
	private final ScheduledFuture<?> _task;
	
	public TimerHolder(T event, StatsSet params, long time, L2Npc npc, L2PcInstance player, boolean isRepeating, IEventTimerEvent<T> eventScript, IEventTimerCancel<T> cancelScript, TimerExecutor<T> postExecutor)
	{
		Objects.requireNonNull(event, getClass().getSimpleName() + ": \"event\" cannot be null!");
		Objects.requireNonNull(eventScript, getClass().getSimpleName() + ": \"script\" cannot be null!");
		Objects.requireNonNull(postExecutor, getClass().getSimpleName() + ": \"postExecutor\" cannot be null!");
		_event = event;
		_params = params;
		_time = time;
		_npc = npc;
		_player = player;
		_isRepeating = isRepeating;
		_eventScript = eventScript;
		_cancelScript = cancelScript;
		_postExecutor = postExecutor;
		_task = isRepeating ? ThreadPoolManager.getInstance().scheduleEventAtFixedRate(this, _time, _time) : ThreadPoolManager.getInstance().scheduleEvent(this, _time);
		TimersManager.getInstance().registerTimer(this);
	}
	
	/**
	 * @return the event/key of this timer
	 */
	public T getEvent()
	{
		return _event;
	}
	
	/**
	 * @return the parameters of this timer
	 */
	public StatsSet getParams()
	{
		return _params;
	}
	
	/**
	 * @return the npc of this timer
	 */
	public L2Npc getNpc()
	{
		return _npc;
	}
	
	/**
	 * @return the player of this timer
	 */
	public L2PcInstance getPlayer()
	{
		return _player;
	}
	
	/**
	 * @return {@code true} if the timer will repeat itself, {@code false} otherwise
	 */
	public boolean isRepeating()
	{
		return _isRepeating;
	}
	
	/**
	 * @return {@code true} if timer for the given event, npc, player were stopped, {@code false} otheriwse
	 */
	public boolean cancelTimer()
	{
		if (_task.isCancelled() || _task.isDone())
		{
			return false;
		}
		
		_task.cancel(false);
		_cancelScript.onTimerCancel(this);
		return true;
	}
	
	/**
	 * @return the remaining time of the timer, or -1 in case it doesn't exists.
	 */
	public long getRemainingTime()
	{
		if (_task == null)
		{
			return -1;
		}
		
		if (_task.isCancelled() || _task.isDone())
		{
			return -1;
		}
		return _task.getDelay(TimeUnit.MILLISECONDS);
	}
	
	/**
	 * @param event
	 * @param npc
	 * @param player
	 * @return {@code true} if event, npc, player are equals to the ones stored in this TimerHolder, {@code false} otherwise
	 */
	public boolean isEqual(T event, L2Npc npc, L2PcInstance player)
	{
		return _event.equals(event) && (_npc == npc) && (_player == player);
	}
	
	@Override
	public void run()
	{
		// Notify the post executor to remove this timer from the map
		_postExecutor.onTimerPostExecute(this);
		
		// Notify the script that the event has been fired.
		_eventScript.onTimerEvent(this);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		
		if (!(obj instanceof TimerHolder))
		{
			return false;
		}
		
		@SuppressWarnings("unchecked")
		final TimerHolder<T> holder = (TimerHolder<T>) obj;
		return isEqual(holder._event, holder._npc, holder._player);
	}
	
	@Override
	public String toString()
	{
		return "event: " + _event + " params: " + _params + " time: " + _time + " npc: " + _npc + " player: " + _player + " repeating: " + _isRepeating + " script: " + _eventScript.getClass().getSimpleName() + " postExecutor: " + _postExecutor.getClass().getSimpleName();
	}
}