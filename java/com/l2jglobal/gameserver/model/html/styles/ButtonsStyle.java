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
package com.l2jglobal.gameserver.model.html.styles;

import com.l2jglobal.gameserver.model.html.IHtmlStyle;

/**
 * @author UnAfraid
 */
public class ButtonsStyle implements IHtmlStyle
{
	private static final String DEFAULT_PAGE_LINK_FORMAT = "<td><button action=\"%s\" value=\"%s\" width=\"%d\" height=\"%d\" back=\"%s\" fore=\"%s\"></td>";
	private static final String DEFAULT_PAGE_TEXT_FORMAT = "<td>%s</td>";
	private static final String DEFAULT_PAGER_SEPARATOR = "<td align=center> | </td>";
	
	public static final ButtonsStyle INSTANCE = new ButtonsStyle(40, 15, "L2UI_CT1.Button_DF", "L2UI_CT1.Button_DF");
	
	private final int _width;
	private final int _height;
	private final String _back;
	private final String _fore;
	
	public ButtonsStyle(int width, int height, String back, String fore)
	{
		_width = width;
		_height = height;
		_back = back;
		_fore = fore;
	}
	
	@Override
	public String applyBypass(String bypass, String name, boolean isEnabled)
	{
		if (isEnabled)
		{
			return String.format(DEFAULT_PAGE_TEXT_FORMAT, name);
		}
		return String.format(DEFAULT_PAGE_LINK_FORMAT, bypass, name, _width, _height, _back, _fore);
	}
	
	@Override
	public String applySeparator()
	{
		return DEFAULT_PAGER_SEPARATOR;
	}
}
