package com.dbn.common.locale;

import com.dbn.common.ui.Presentable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.DateFormat;

import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum DBDateFormat implements Presentable {
    FULL(nls("cfg.shared.const.DateFormat_FULL"), DateFormat.FULL),
    SHORT(nls("cfg.shared.const.DateFormat_SHORT"), DateFormat.SHORT),
    MEDIUM(nls("cfg.shared.const.DateFormat_MEDIUM"), DateFormat.MEDIUM),
    LONG(nls("cfg.shared.const.DateFormat_LONG"), DateFormat.LONG),
    CUSTOM(nls("cfg.shared.const.DateFormat_CUSTOM"), 0);

    private final String name;
    private final int format;
}
