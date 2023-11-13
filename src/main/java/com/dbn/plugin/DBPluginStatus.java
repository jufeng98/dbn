package com.dbn.plugin;

public enum DBPluginStatus {
    UNKNOWN, // state not yet evaluated
    MISSING, // not installed
    PASSIVE, // installed but no connections configured
    ACTIVE   // installed and connections configured
}
