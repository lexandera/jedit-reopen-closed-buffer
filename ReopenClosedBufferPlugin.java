/*
 * ReopenClosedBufferPlugin.java
 * part of the ReopenClosedBuffer plugin for the jEdit text editor
 * Copyright (C) 2013 Aleksander Kmetec
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import java.util.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.msg.BufferUpdate;

/**
 * The ReopenClosedBuffer plugin
 * 
 * @author Aleksander Kmetec
 */
public class ReopenClosedBufferPlugin extends EBPlugin {
    public static final String NAME = "reopenclosedbuffer";
    public static final String OPTION_PREFIX = "options.reopenclosedbuffer.";
    public static ReopenClosedBufferPlugin instance;
    private Map<View, Stack<String>> perViewPathStack = new HashMap<View, Stack<String>>();
    
    public ReopenClosedBufferPlugin() {
        instance = this;
    }
    
    public void handleMessage(EBMessage message) {
        if (message instanceof BufferUpdate) {
            BufferUpdate bumsg = (BufferUpdate)message;
            if (bumsg.getWhat() == BufferUpdate.CLOSED) {
                View view = bumsg.getView();
                String path = bumsg.getBuffer().getPath();
                if (view != null && path != null) {
                    if (!perViewPathStack.containsKey(view)) {
                        perViewPathStack.put(view, new Stack<String>());
                    }
                    perViewPathStack.get(view).push(path);
                }
            }
        }
    }
    
    public void reopenClosedBuffer() {
        View view = jEdit.getActiveView();
        Stack<String> pathStack = perViewPathStack.get(view);
        
        if (pathStack != null && !pathStack.empty()) {
            jEdit.openFile(view, pathStack.pop());
        } else {
            showMsg("No buffer to reopen.");
        }
    }
    
    private void showMsg(String msg) {
        jEdit.getActiveView().getStatus().setMessageAndClear(msg);
    }
}

