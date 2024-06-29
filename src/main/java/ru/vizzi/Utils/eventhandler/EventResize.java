package ru.vizzi.Utils.eventhandler;

import cpw.mods.fml.common.eventhandler.Event;
public class EventResize extends Event {

    final int width;
    final int height;

    public EventResize(int width, int height) {
        this.width = width;
        this.height = height;
    }

}

