package com.dbn.editor.session.color;

import com.intellij.openapi.editor.colors.TextAttributesKey;

public interface SessionBrowserTextAttributesKeys {
    TextAttributesKey DEFAULT_ACTIVE_SESSION    = TextAttributesKey.createTextAttributesKey("DBNavigator.DefaultTextAttributes.SessionBrowser.ActiveSession");
    TextAttributesKey DEFAULT_INACTIVE_SESSION  = TextAttributesKey.createTextAttributesKey("DBNavigator.DefaultTextAttributes.SessionBrowser.InactiveSession");
    TextAttributesKey DEFAULT_CACHED_SESSION    = TextAttributesKey.createTextAttributesKey("DBNavigator.DefaultTextAttributes.SessionBrowser.CachedSession");
    TextAttributesKey DEFAULT_SNIPED_SESSION    = TextAttributesKey.createTextAttributesKey("DBNavigator.DefaultTextAttributes.SessionBrowser.SnipedSession");
    TextAttributesKey DEFAULT_KILLED_SESSION    = TextAttributesKey.createTextAttributesKey("DBNavigator.DefaultTextAttributes.SessionBrowser.KilledSession");

    TextAttributesKey ACTIVE_SESSION   = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.SessionBrowser.ActiveSession",   DEFAULT_ACTIVE_SESSION);
    TextAttributesKey INACTIVE_SESSION = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.SessionBrowser.InactiveSession", DEFAULT_INACTIVE_SESSION);
    TextAttributesKey CACHED_SESSION   = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.SessionBrowser.CachedSession",   DEFAULT_CACHED_SESSION);
    TextAttributesKey SNIPED_SESSION   = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.SessionBrowser.SnipedSession",   DEFAULT_SNIPED_SESSION);
    TextAttributesKey KILLED_SESSION   = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.SessionBrowser.KilledSession",   DEFAULT_KILLED_SESSION);
}

