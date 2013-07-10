/**
 * iCal Import/Export - import ical-Files on Android
 * 
 * Copyright (C) 2013 Gilles Baatz <baatzgilles@gmail.com>
 * Copyright (C) 2013 Dominik Schürmann <dominik@dominikschuermann.de>
 * Copyright (C) 2010-2011 Lukas Aichbauer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.aichbauer.ical;

public interface ICalConstants {
    public static final String HELP = "<b>iCal Import/Export Plus</b><br>"
            + "It is a simple tool to import iCal files into your Android calendar."
            + "<br>To successfully import iCal events please follow the given steps below:<br><br>"
            + "  +<i>Select a calendar</i><br><small>The selected calendar will be edited</small><br>"
            + "  +<i>Search iCal files</i> or <i>Set URL</i><br><small>Searches the SD card for iCal files or enter URL of an iCal file directly (http and ftp support)</small><br>"
            + "  +<i>Select an iCal file</i><br>"
            + "  +<i>Load iCal file</i><br><small>The iCal file will be parsed, if successfull a number of events should appear next to the button</small><br>"
            + "  +<i>Insert events</i><br><small>The events from the selected iCal file will be inserted into the selected calendar. If the checkbox <i>ignore duplicates</i> is checked, then the events, which are already in the calendar, will not be inserted another time. When finished a status information should be displayed.</small><br>"
            + "<br>"
            + "Additional features:<br>"
            + "  +<i>Delete events</i><br><small>The events from the selected iCal file will be removed from the selected calendar. When finished a status information should be displayed.</small><br>"
            + "  +<i>Save to iCal File</i><br><small>All events from the selected calendar will be saved to the selected iCal File</small><br>"
            + "<br>"
            + "If you are considering errors, please add a new issue to <a href=\"https://github.com/gillesB/ical-import-export\">the developer's homepage</a> or write a mail to the <a href=\"mailto:baatzgilles@gmail.com\">developper</a> and provide an iCal file you would like to import.<br>"
            + "<br>Thanks to iCal4j Project for the parser/interpreter and to Lukas Aichbauer who provided the original application.<br><br>"
            + "<i>To view this information again: menu --> help</i>";
    
}
