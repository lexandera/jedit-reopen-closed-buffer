/*
 * ReopenClosedBufferPlugin.java
 * part of the ReopenClosedBuffer plugin for the jEdit text editor
 * Copyright (C) 2001 Aleksander Kmetec
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
 *
 * $Id$
 */

import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.msg.BufferUpdate;
import java.util.Stack;

/**
 * The ReopenClosedBuffer plugin
 * 
 * @author Aleksander Kmetec
 */
public class ReopenClosedBufferPlugin extends EBPlugin {
    public static final String NAME = "reopenclosedbuffer";
    public static final String OPTION_PREFIX = "options.reopenclosedbuffer.";
    
    public static ReopenClosedBufferPlugin instance;
    
    private Stack<String> pathStack = new Stack<String>();
    
    public ReopenClosedBufferPlugin() {
        instance = this;
    }
    
    public void handleMessage(EBMessage message) {
        if (message instanceof BufferUpdate) {
            BufferUpdate bumsg = (BufferUpdate)message;
            if (bumsg.getWhat() == BufferUpdate.CLOSED) {
                String path = bumsg.getBuffer().getPath();
                pathStack.push(path);
            }
        }
    }
    
    public void reopenClosedBuffer() {
        if (!pathStack.empty()) {
            String path = pathStack.pop();
            jEdit.openFile(jEdit.getActiveView(), path);
        } else {
            showMsg("No buffer to reopen.");
        }
    }
    
    private void showMsg(String msg) {
        jEdit.getActiveView().getStatus().setMessageAndClear(msg);
    }
}

